package com.medwell.ambulance.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.dto.AmbulanceLocationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;

import java.util.*;

@Component
@Slf4j
public class RedisGeoLocationService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

//    ambulances will update their real time location and save to redis
    public void setLocationOfAmbulance(AmbulanceLocationDTO ambulanceLocationDTO){
        redisTemplate.opsForGeo().add("ambulance-locations", new Point(ambulanceLocationDTO.getLongitude(), ambulanceLocationDTO.getLatitude()), ambulanceLocationDTO.getAmbulanceId());
        log.info("Saved real time location of ambulance in redis");
    }

//    this will be used when ambulance is booked
    @Async
    public void removeAmbulanceGeoData(String ambulanceId){
        redisTemplate.opsForZSet().remove("ambulance-locations", ambulanceId);
    }

//    to get list of nearby ambulances
public List<Map<String, Object>> getNearbyAmbulanceData(Double lat, Double lon) {
    Circle circle = new Circle(new Point(lon, lat), new Distance(2, Metrics.KILOMETERS));


    RedisGeoCommands.GeoRadiusCommandArgs args = GeoRadiusCommandArgs.newGeoRadiusArgs()
            .includeDistance()
            .sortAscending();

    GeoResults<GeoLocation<String>> results = redisTemplate.opsForGeo()
            .radius("ambulance-locations", circle, args);

    List<Map<String, Object>> nearbyAmbulances = new ArrayList<>();

    if (results != null) {
        for (GeoResult<GeoLocation<String>> result : results) {
            String ambulanceId = result.getContent().getName();
            String ambulanceType = getAmbulanceType(ambulanceId);
                Map<String, Object> ambulance = new HashMap<>();
                ambulance.put("type", ambulanceType);
                ambulance.put("ambulanceId", ambulanceId);
                ambulance.put("distanceKm", result.getDistance().getValue());
                ambulance.put("location", getCurrentLocationOfAvailableAmbulance(ambulanceId));

                nearbyAmbulances.add(ambulance);
            }

        }


        return nearbyAmbulances;
    }

    public List<Map<String,String>> getNearbyAmbulancesByType(String type, Double lat, Double lon){
        Circle circle = new Circle(new Point(lon, lat), new Distance(2, Metrics.KILOMETERS));

        // Use RadiusOptions to request distances
        RedisGeoCommands.GeoRadiusCommandArgs args = GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()
                .sortAscending();   // Optional: Sort by nearest first

        GeoResults<GeoLocation<String>> results = redisTemplate.opsForGeo()
                .radius("ambulance-locations", circle, args);

        List<Map<String,String>> nearbyAmbulances = new ArrayList<>();

        if (results != null) {
            for (GeoResult<GeoLocation<String>> result : results) {
                String ambulanceId = result.getContent().getName();
                String ambulanceType = getAmbulanceType(ambulanceId);
                if(ambulanceType.equals(type))
                    nearbyAmbulances.add(
                            Map.of("ambulanceId",ambulanceId,"distance",String.valueOf(result.getDistance().getValue()))
                    );

            }

        }
        return nearbyAmbulances;

    }


//    will be used for websocket to show nearby ambulances move in real time
    public Map<String,Double> getCurrentLocationOfAvailableAmbulance(String ambulanceId){
        Point coordinates = redisTemplate.opsForGeo().position("ambulance-locations", ambulanceId).get(0);
        return Map.of(
                "latitude", coordinates.getY(),  // Lat
                "longitude", coordinates.getX() // Lon
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



