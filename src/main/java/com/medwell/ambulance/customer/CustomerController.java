package com.medwell.ambulance.customer;


import com.medwell.ambulance.utils.RedisUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private RedisUtility redisUtility;

//    send request every 10 mins
    @GetMapping("/get-nearby-ambulances/{latitude}/{longitude}")
    public ResponseEntity<?> getNearbyAmbulances(@PathVariable("latitude") Double lat,@PathVariable("longitude") Double lon){
        List<Map<String, Object>>  resp=redisUtility.getNearbyAmbulanceData(lat, lon);
        return ResponseEntity.status(200).body(resp);

    }





}
