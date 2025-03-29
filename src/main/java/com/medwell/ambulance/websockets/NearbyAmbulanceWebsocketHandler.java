package com.medwell.ambulance.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.dto.NearbyAmbulanceDTO;
import com.medwell.ambulance.utils.RedisUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class NearbyAmbulanceWebsocketHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisUtility redisUtility;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        NearbyAmbulanceDTO nearbyAmbulanceDTO=objectMapper.readValue(message.getPayload(), NearbyAmbulanceDTO.class);
        List<Map<String,Object>> ambulanceList=new ArrayList<>();
        for(String id:nearbyAmbulanceDTO.getIdList()){

            ambulanceList.add(Map.of(
                    "ambulanceId",id,
                    "location",redisUtility.getCurrentLocationOfAvailableAmbulance(id)
            ));

        }
        String responseJson = objectMapper.writeValueAsString(ambulanceList);

        // Send response back to client
        session.sendMessage(new TextMessage(responseJson));
    }
}
