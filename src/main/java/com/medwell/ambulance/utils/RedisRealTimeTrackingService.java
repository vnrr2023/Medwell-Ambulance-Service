package com.medwell.ambulance.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisRealTimeTrackingService {

    @Autowired
    private RedisTemplate<String ,String> redisTemplate;

    public void setCurrentLocationOfAmbulance(String bookingId,String data){
        String key="booking-current-location:"+bookingId;
        redisTemplate.opsForValue().set(key,data);
    }

    public String getCurrentLocationOfAmbulance(String bookingId){
        String key="booking-current-location:"+bookingId;
        return redisTemplate.opsForValue().get(key);
    }



}
