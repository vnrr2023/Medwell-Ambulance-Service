package com.medwell.ambulance.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.dto.AmbulanceLocationDTO;
import com.medwell.ambulance.utils.RedisGeoLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AmbulanceConsumerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisGeoLocationService redisGeoLocationService;


    @KafkaListener(topics = "ambulance-locations",groupId = "my-spring-group")
    public void consumeAmbulanceLocation(String message)  {
        try {
            AmbulanceLocationDTO locationDTO = objectMapper.readValue(message, AmbulanceLocationDTO.class);
            redisGeoLocationService.setLocationOfAmbulance(locationDTO);
        } catch (Exception e) {
            log.error("Failed to read Json data as it is null");
        }
    }

}
