//package com.medwell.ambulance.ambulance;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.medwell.ambulance.dto.AmbulanceLocationDTO;
//import com.medwell.ambulance.utils.RedisUtility;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AmbulanceConsumerService {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private RedisUtility redisUtility;
//
//
//    @KafkaListener(topics = "ambulance-locations",groupId = "group1")
//    public void consumeAmbulanceLocatio(String message) throws Exception {
//        AmbulanceLocationDTO locationDTO=objectMapper.readValue(message, AmbulanceLocationDTO.class);
//        redisUtility.setLocationOfAmbulance(locationDTO);
//    }
//
//
//}
