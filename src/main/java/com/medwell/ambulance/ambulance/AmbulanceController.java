package com.medwell.ambulance.ambulance;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.medwell.ambulance.dto.*;
import com.medwell.ambulance.entity.Ambulance;
import com.medwell.ambulance.utils.RedisGeoLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ambulance")
public class AmbulanceController {

    @Autowired
    private AmbulanceService ambulanceService;

    @Autowired
    private RedisGeoLocationService redisGeoLocationService;

    @PostMapping("/accept-booking")
    public ResponseEntity<?> acceptBooking(@RequestBody AceeptBookingRequestDTO bookingRequestDTO) throws JsonProcessingException {
        BookingResponseDTO responseDTO= ambulanceService.acceptBookingRequest(bookingRequestDTO.getAmbulanceId(),bookingRequestDTO.getBookingId(),bookingRequestDTO.getRequestId(),bookingRequestDTO.getOtherAmbulances(),bookingRequestDTO.getLatitude(),bookingRequestDTO.getLongitude());
        return ResponseEntity.status(201).body(responseDTO);

    }

    @PostMapping("/send-type")
    public ResponseEntity<?> sendAmbulanceType(@RequestParam("type") String type,
                                               @RequestParam("ambulanceId") String ambulanceId){

        redisGeoLocationService.setAmbulanceType(ambulanceId,type);
        return ResponseEntity.status(200).build();

    }

    @PostMapping("/complete-profile")
    public ResponseEntity<?> completeAmbulanceProfile(@RequestBody AmbulanceProfileDTO ambulanceProfileDTO){

        Ambulance ambulance=ambulanceService.addAmbulanceData(ambulanceProfileDTO.getAmbulanceType(),ambulanceProfileDTO.getAmbulanceBrandName(),ambulanceProfileDTO.getNumberPlate(),ambulanceProfileDTO.getUserId());

        return ResponseEntity.status(201).body(ambulance);

    }

    @PostMapping("/update-booking-status")
    public ResponseEntity<?> updateBookingStatus(@RequestBody BookingStatusRequestDTO bookingStatusRequestDTO){
        ambulanceService.updateBookingStatusOfLocation(bookingStatusRequestDTO.getUpdatedStatus(),
                bookingStatusRequestDTO.getBookingId()
        );
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/set-dropoff-location")
    public ResponseEntity<?> setDropOffLocation(@RequestBody DropOffLocationRequestDTO dropOffLocationRequestDTO){

        String linkTodestination=ambulanceService.setDropOffLocationOfBooking(dropOffLocationRequestDTO.getBookingId(),dropOffLocationRequestDTO.getLat(),dropOffLocationRequestDTO.getLon());
        return ResponseEntity.status(201).body(Map.of("gmapsUrl",linkTodestination));
    }








}
