package com.medwell.ambulance.ambulance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ambulance")
public class AmbulanceController {

    @Autowired
    private AmbulanceService ambulanceService;

    @PostMapping("/")
    public ResponseEntity<?> updateAmabulanceStatus(@RequestParam("status") String status,
                                                    @RequestParam("ambulanceId") String ambulanceId){

        return ResponseEntity.status(200).build();

    }




}
