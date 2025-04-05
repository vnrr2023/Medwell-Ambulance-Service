package com.medwell.ambulance.websockets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.dto.AmbulanceLocationDTO;
import com.medwell.ambulance.utils.RedisUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class AmbulanceWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisUtility redisUtility;


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    AmbulanceLocationDTO ambulanceLocationDTO=objectMapper.readValue(message.getPayload(), AmbulanceLocationDTO.class);
//    kafkaTemplate.send("ambulance-locations",ambulanceLocationDTO.getAmbulanceId(),message.getPayload());
        redisUtility.setLocationOfAmbulance(ambulanceLocationDTO);
    String requests=redisUtility.getAllBookingRequests(ambulanceLocationDTO.getAmbulanceId());
    session.sendMessage(new TextMessage(requests));

    }
}
