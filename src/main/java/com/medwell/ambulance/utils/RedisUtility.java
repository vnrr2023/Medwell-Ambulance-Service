package com.medwell.ambulance.utils;

import com.medwell.ambulance.dto.AmbulanceLocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;

import java.util.*;

@Component
public class RedisUtility {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

//    ambulances will update their real time location and save to redis
    public void setLocationOfAmbulance(AmbulanceLocationDTO ambulanceLocationDTO){
        redisTemplate.opsForGeo().add("ambulance-locations", new Point(ambulanceLocationDTO.getLongitude(), ambulanceLocationDTO.getLatitude()), ambulanceLocationDTO.getAmbulanceId());
        System.out.println("Saved to redis");
    }

//    this will be used when ambulance is booked
    public void removeAmbulanceGeoData(String ambulanceId){
        redisTemplate.opsForZSet().remove("ambulance-locations", ambulanceId);
    }

//    to get list of nearby ambulances
public List<Map<String, Object>> getNearbyAmbulanceData(Double lat, Double lon) {
    Circle circle = new Circle(new Point(lon, lat), new Distance(2, Metrics.KILOMETERS));

    // Use RadiusOptions to request distances
    RedisGeoCommands.GeoRadiusCommandArgs args = GeoRadiusCommandArgs.newGeoRadiusArgs()
            .includeDistance()  // Ensure distances are included
            .sortAscending();   // Optional: Sort by nearest first

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
            ambulance.put("location",
                    getCurrentLocationOfAvailableAmbulance(ambulanceId));

            nearbyAmbulances.add(ambulance);
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



