package com.medwell.ambulance.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.dto.AmbulanceBookingRequestRedisDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisBookingService {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void setBooking(String bookingId){
        String key="booking:"+bookingId;
        redisTemplate.opsForValue().set(key,"false");
        log.info("Inserted booking request in redis");

    }

    @Async
    public void removeBooking(String bookingId){
        String key="booking:"+bookingId;
        redisTemplate.opsForValue().set(key,"true");
        redisTemplate.expire(key, 15L, TimeUnit.MINUTES);
        log.info("Updated booking request in redis");
    }

    public String getAllBookingRequests(String ambulanceId) throws JsonProcessingException
    {
        String key="ambulance-booking-requests:"+ambulanceId;
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) return "";
        List<String> requestJsonList = redisTemplate.opsForList().range(key, 0, -1);
        return objectMapper.writeValueAsString(requestJsonList);
    }

    @Async
    public void removeRequestsFromAmbulances(String requestId,List<String> ambulances) throws JsonProcessingException {

        for(String ambulanceId:ambulances){
            String redisKey="ambulance-booking-requests:"+ambulanceId;
            List<String> requests = redisTemplate.opsForList().range(redisKey, 0, -1);

            if (requests == null || requests.isEmpty()) {
                continue;
            }
            for (String request:requests){
                if ( objectMapper.readValue(request, AmbulanceBookingRequestRedisDTO.class).getRequestId().equals(requestId)){
                    redisTemplate.opsForList().remove(redisKey,1,request);
                    break;
                }
            }

        }

    }


    @Async
    public void setAmbulanceRequest(String request,String ambulanceId){
        String key="ambulance-booking-requests:"+ambulanceId;
        redisTemplate.opsForList().rightPush(key,request);
    }


}
