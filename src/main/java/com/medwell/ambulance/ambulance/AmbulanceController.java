package com.medwell.ambulance.ambulance;


import com.medwell.ambulance.utils.RedisUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ambulance")
public class AmbulanceController {

    @Autowired
    private AmbulanceService ambulanceService;

    @Autowired
    private RedisUtility redisUtility;

    @PostMapping("/")
    public ResponseEntity<?> updateAmabulanceStatus(@RequestParam("status") String status,
                                                    @RequestParam("ambulanceId") String ambulanceId){

        return ResponseEntity.status(200).build();

    }

    @PostMapping("/send-type")
    public ResponseEntity<?> sendAmbulanceType(@RequestParam("type") String type,
                                               @RequestParam("ambulanceId") String ambulanceId){

        redisUtility.setAmbulanceType(ambulanceId,type);
        return ResponseEntity.status(200).build();

    }





}
