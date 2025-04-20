package com.medwell.ambulance.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.dto.RealTimeLocationUpdatesDTO;
import com.medwell.ambulance.utils.RedisRealTimeTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookingDataConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisRealTimeTrackingService redisRealTimeTrackingService;


    @KafkaListener(topics = "booking-real-time-updates",groupId = "group1")
    public void consumeLocationOfAmbulance(String message)  {
        try {
            RealTimeLocationUpdatesDTO realTimeLocationUpdatesDTO = objectMapper.readValue(message, RealTimeLocationUpdatesDTO.class);
            redisRealTimeTrackingService.setCurrentLocationOfAmbulance(realTimeLocationUpdatesDTO.getBookingId(), message);
            log.info("Saved real time booking data in redis");
        }catch (Exception e){
            log.error("Failed to parse Json data as it is null");
        }
    }

}
