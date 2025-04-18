package com.medwell.ambulance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<?> getHealthOfServer(){
        return ResponseEntity.status(200).body(Map.of("message","Server is Up and running"));
    }

}
