package com.medwell.ambulance.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.dto.RealTimeLocationUpdatesDTO;
import com.medwell.ambulance.utils.RedisRealTimeTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingDataConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisRealTimeTrackingService redisRealTimeTrackingService;


    @KafkaListener(topics = "booking-real-time-updates",groupId = "group1")
    public void consumeLocationOfAmbulance(String message) throws JsonProcessingException {
        RealTimeLocationUpdatesDTO realTimeLocationUpdatesDTO=objectMapper.readValue(message, RealTimeLocationUpdatesDTO.class);
        redisRealTimeTrackingService.setCurrentLocationOfAmbulance(realTimeLocationUpdatesDTO.getBookingId(),message);
        System.out.println("saved real time data in redis");
    }



}
