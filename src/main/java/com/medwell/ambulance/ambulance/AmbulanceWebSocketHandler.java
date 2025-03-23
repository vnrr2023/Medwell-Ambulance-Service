package com.medwell.ambulance.ambulance;


import com.fasterxml.jackson.databind.ObjectMapper;
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


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    AmbulanceLocationDTO ambulanceLocationDTO=objectMapper.readValue(message.getPayload(), AmbulanceLocationDTO.class);
    kafkaTemplate.send("ambulance-locations",ambulanceLocationDTO.getAmbulanceId(),message.getPayload());

    }
}
