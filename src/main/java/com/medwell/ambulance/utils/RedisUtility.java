package com.medwell.ambulance.utils;



import com.medwell.ambulance.ambulance.AmbulanceLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RedisUtility {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

//    ambulances will update their real time location and save to redis
    public void setLocationOfAmbulance(AmbulanceLocationDTO ambulanceLocationDTO){
        redisTemplate.opsForGeo().add("ambulance-locations", new Point(ambulanceLocationDTO.getLongitude(), ambulanceLocationDTO.getLatitude()), ambulanceLocationDTO.getAmbulanceId());
    }

//    this will be used when ambulance is booked
    public void removeAmbulanceGeoData(String ambulanceId){
        redisTemplate.opsForZSet().remove("ambulance-locations", ambulanceId);
    }

//    to get list of nearby ambulances
    public List<Map<String, Object>> getNearbyAmbulanceData(Double lat,Double lon){


        Circle circle = new Circle(new Point(lon, lat), new Distance(2, Metrics.KILOMETERS));

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo()
                .radius("ambulance-locations", circle);

        List<Map<String, Object>> nearbyAmbulances = new ArrayList<>();

        if (results != null) {
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
                String ambulanceId = result.getContent().getName();
                String ambulanceType=getAmbulanceType(ambulanceId);
                Map<String, Object> ambulance = new HashMap<>();
                ambulance.put("type",ambulanceType);
                ambulance.put("ambulanceId", ambulanceId);
                ambulance.put("distanceMeters", result.getDistance().getValue());

                nearbyAmbulances.add(ambulance);
            }
        }

        return nearbyAmbulances;

    }

//    will be used for websocket to show nearby ambulances move in real time
    public Map<String,Double> getCurrentLocationOfAvailableAmbulance(String ambulanceId){
        Point coordinates = redisTemplate.opsForGeo().position("ambulance-locations", ambulanceId).get(0);
        return Map.of(
                "lat", coordinates.getY(),  // Lat
                "lon", coordinates.getX() // Lon
        );
    }


//    when the ambulance starts it will put the type
    public void setAmbulanceType(String ambulanceId,String ambulanceType){
        redisTemplate.opsForValue().set("ambulance-type:"+ambulanceId,ambulanceType);
    }

//    to get ambulance type
    public String getAmbulanceType(String ambulanceId){
        return redisTemplate.opsForValue().get("ambulance-type:"+ambulanceId);
    }



}



