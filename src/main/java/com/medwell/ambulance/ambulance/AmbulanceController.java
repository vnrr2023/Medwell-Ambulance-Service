package com.medwell.ambulance.ambulance;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.medwell.ambulance.dto.AceeptBookingRequestDTO;
import com.medwell.ambulance.dto.AmbulanceProfileDTO;
import com.medwell.ambulance.entity.Ambulance;
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

    @PostMapping("/accept-booking")
    public ResponseEntity<?> acceptBooking(@RequestBody AceeptBookingRequestDTO bookingRequestDTO) throws JsonProcessingException {
            ambulanceService.acceptBookingRequest(bookingRequestDTO.getAmbulanceId(),bookingRequestDTO.getBookingId(),bookingRequestDTO.getRequestId(),bookingRequestDTO.getOtherAmbulances());
        return ResponseEntity.status(200).build();

    }

    @PostMapping("/send-type")
    public ResponseEntity<?> sendAmbulanceType(@RequestParam("type") String type,
                                               @RequestParam("ambulanceId") String ambulanceId){

        redisUtility.setAmbulanceType(ambulanceId,type);
        return ResponseEntity.status(200).build();

    }

    @PostMapping("/complete-profile")
    public ResponseEntity<?> completeAmbulanceProfile(@RequestBody AmbulanceProfileDTO ambulanceProfileDTO){

        Ambulance ambulance=ambulanceService.addAmbulanceData(ambulanceProfileDTO.getAmbulanceType(),ambulanceProfileDTO.getAmbulanceBrandName(),ambulanceProfileDTO.getNumberPlate(),ambulanceProfileDTO.getUserId());

        return ResponseEntity.status(201).body(ambulance);

    }






}
