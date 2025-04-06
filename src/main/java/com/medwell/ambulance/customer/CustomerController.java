package com.medwell.ambulance.customer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.ambulance.AmbulanceService;
import com.medwell.ambulance.dto.BookingRequestDTO;
import com.medwell.ambulance.dto.DropOffLocationRequestDTO;
import com.medwell.ambulance.dto.RealTimeLocationUpdatesDTO;
import com.medwell.ambulance.utils.RedisGeoLocationService;
import com.medwell.ambulance.utils.RedisRealTimeTrackingService;
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

    @Autowired
    private AmbulanceService ambulanceService;

    @Autowired
    private RedisRealTimeTrackingService realTimeTrackingService;

    @Autowired
    private ObjectMapper objectMapper;

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

    @PostMapping("/set-dropoff-location")
    public ResponseEntity<?> setDropOffLocation(@RequestBody DropOffLocationRequestDTO dropOffLocationRequestDTO){

        ambulanceService.setDropOffLocationOfBooking(dropOffLocationRequestDTO.getBookingId(),dropOffLocationRequestDTO.getLat(),dropOffLocationRequestDTO.getLon());
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/current-location-ambulance/{bookingId}")
    public ResponseEntity<?> getCurrentLocationOfAmbulance(@PathVariable("bookingId") String bookingId) throws JsonProcessingException {
        String data=realTimeTrackingService.getCurrentLocationOfAmbulance(bookingId);
        RealTimeLocationUpdatesDTO realTimeLocationUpdatesDTO=objectMapper.readValue(data, RealTimeLocationUpdatesDTO.class);
        return ResponseEntity.status(200).body(realTimeLocationUpdatesDTO);
    }


}
