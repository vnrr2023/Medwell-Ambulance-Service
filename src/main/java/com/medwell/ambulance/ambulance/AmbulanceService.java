package com.medwell.ambulance.ambulance;


import com.medwell.ambulance.utils.RedisUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmbulanceService {

    @Autowired
    private RedisUtility redisUtility;

    public void removeAmbulance(String ambulanceId){
        redisUtility.removeAmbulanceGeoData(ambulanceId);
    }


}
