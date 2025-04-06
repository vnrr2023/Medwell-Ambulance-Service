package com.medwell.ambulance.websockets;

import com.medwell.ambulance.dto.AmbulanceLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class BookingAmbulanceWebSocketHandler extends TextWebSocketHandler {


    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        /*
        message format -> {"bookingId":"<id>","lat":the lat not string , "lon":the lon not string}
         */

        kafkaTemplate.send("booking-real-time-updates", message.getPayload());
        session.sendMessage(new TextMessage("201"));

    }

}
