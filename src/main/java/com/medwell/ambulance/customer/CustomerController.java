package com.medwell.ambulance.customer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.medwell.ambulance.dto.BookingRequestDTO;
import com.medwell.ambulance.utils.RedisGeoLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private RedisGeoLocationService redisGeoLocationService;

    @Autowired
    private CustomerService customerService;

//    send request every 10 mins
    @GetMapping("/get-nearby-ambulances/{latitude}/{longitude}")
    public ResponseEntity<?> getNearbyAmbulances(@PathVariable("latitude") Double lat,@PathVariable("longitude") Double lon){

        List<Map<String, Object>>  resp= redisGeoLocationService.getNearbyAmbulanceData(lat, lon);
        return ResponseEntity.status(200).body(resp);

    }

    @PostMapping("/request-booking")
    public ResponseEntity<?> createRequest(@RequestBody BookingRequestDTO bookingRequestDTO) throws JsonProcessingException {

        String bookingId=customerService.createBooking(bookingRequestDTO.getCustomerId(),bookingRequestDTO.getLat(),bookingRequestDTO.getLon(),bookingRequestDTO.getAmbulanceType());
        return ResponseEntity.status(201).body(Map.of("bookingId",bookingId));
    }








}
