package com.medwell.ambulance.ambulance;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.medwell.ambulance.dto.*;
import com.medwell.ambulance.entity.Ambulance;
import com.medwell.ambulance.exceptions.AmbulanceCustomException;
import com.medwell.ambulance.exceptions.ErrorResponseDTO;
import com.medwell.ambulance.utils.RedisGeoLocationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/ambulance")
public class AmbulanceController {

    @Autowired
    private AmbulanceService ambulanceService;

    @Autowired
    private RedisGeoLocationService redisGeoLocationService;

    @PostMapping("/accept-booking")
    public ResponseEntity<?> acceptBooking(@RequestBody AceeptBookingRequestDTO bookingRequestDTO) {
        try {
            BookingResponseDTO responseDTO= ambulanceService.acceptBookingRequest(bookingRequestDTO.getAmbulanceId(),bookingRequestDTO.getBookingId(),bookingRequestDTO.getRequestId(),bookingRequestDTO.getOtherAmbulances(),bookingRequestDTO.getLatitude(),bookingRequestDTO.getLongitude());
            return ResponseEntity.status(201).body(responseDTO);
        } catch (JsonProcessingException e) {
            throw new AmbulanceCustomException("Failed to accept booking",400,e.getMessage());
        }

    }

    @PostMapping("/send-type")
    public ResponseEntity<?> sendAmbulanceType(@RequestParam("type") String type,
                                               @RequestParam("ambulanceId") String ambulanceId){

        try {
            redisGeoLocationService.setAmbulanceType(ambulanceId,type);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            throw new AmbulanceCustomException("Failed to send type",400,e.getMessage());
        }

    }

    @PostMapping("/complete-profile")
    public ResponseEntity<?> completeAmbulanceProfile(@RequestBody AmbulanceProfileDTO ambulanceProfileDTO){

        try {
            Ambulance ambulance=ambulanceService.addAmbulanceData(ambulanceProfileDTO.getAmbulanceType(),ambulanceProfileDTO.getAmbulanceBrandName(),ambulanceProfileDTO.getNumberPlate(),ambulanceProfileDTO.getUserId());

            return ResponseEntity.status(201).body(ambulance);
        } catch (Exception e) {
            throw new AmbulanceCustomException("Failed to complete profile",400,e.getMessage());
        }

    }

    @PostMapping("/update-booking-status")
    public ResponseEntity<?> updateBookingStatus(@RequestBody BookingStatusRequestDTO bookingStatusRequestDTO){
        try {
            ambulanceService.updateBookingStatusOfLocation(bookingStatusRequestDTO.getUpdatedStatus(),
                    bookingStatusRequestDTO.getBookingId()
            );
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            throw new AmbulanceCustomException("Failed to update booking status",400,e.getMessage());
        }
    }

    @PostMapping("/set-dropoff-location")
    public ResponseEntity<?> setDropOffLocation(@RequestBody DropOffLocationRequestDTO dropOffLocationRequestDTO){
        try {
            String linkTodestination = ambulanceService.setDropOffLocationOfBooking(dropOffLocationRequestDTO.getBookingId(), dropOffLocationRequestDTO.getLat(), dropOffLocationRequestDTO.getLon());
            return ResponseEntity.status(201).body(Map.of("gmapsUrl", linkTodestination));
        } catch (Exception e) {
            throw new AmbulanceCustomException("Failed to set drop off location",400,e.getMessage());
        }
    }


    @ExceptionHandler(AmbulanceCustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(AmbulanceCustomException exception) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                exception.getMessage(),
                exception.getStatus()
        );
        log.error("{} | Details: {}", exception.getMessage(), exception.getExceptionMessage());
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }








}
