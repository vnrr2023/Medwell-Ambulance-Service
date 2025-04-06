package com.medwell.ambulance.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.dto.AmbulanceLocationDTO;
import com.medwell.ambulance.utils.RedisGeoLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AmbulanceConsumerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisGeoLocationService redisGeoLocationService;


    @KafkaListener(topics = "ambulance-locations",groupId = "group1")
    public void consumeAmbulanceLocatio(String message) throws Exception {
        AmbulanceLocationDTO locationDTO=objectMapper.readValue(message, AmbulanceLocationDTO.class);
        redisGeoLocationService.setLocationOfAmbulance(locationDTO);
    }

}
