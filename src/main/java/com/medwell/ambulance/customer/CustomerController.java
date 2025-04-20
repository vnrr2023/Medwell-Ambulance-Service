package com.medwell.ambulance.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.ambulance.AmbulanceService;
import com.medwell.ambulance.dto.BookingRequestDTO;
import com.medwell.ambulance.dto.DropOffLocationRequestDTO;
import com.medwell.ambulance.dto.RealTimeLocationUpdatesDTO;
import com.medwell.ambulance.exceptions.AmbulanceCustomException;
import com.medwell.ambulance.exceptions.ErrorResponseDTO;
import com.medwell.ambulance.utils.RedisGeoLocationService;
import com.medwell.ambulance.utils.RedisRealTimeTrackingService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
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

        try {
            List<Map<String, Object>>  resp= redisGeoLocationService.getNearbyAmbulanceData(lat, lon);
            return ResponseEntity.status(200).body(resp);
        } catch (Exception e) {
            throw new AmbulanceCustomException("Failed to get nearby ambulances",400,e.getMessage());
        }

    }

    @PostMapping("/request-booking")
    public ResponseEntity<?> createRequest(@RequestBody BookingRequestDTO bookingRequestDTO) {

        try {
            String bookingId=customerService.createBooking(bookingRequestDTO.getCustomerId(),bookingRequestDTO.getLat(),bookingRequestDTO.getLon(),bookingRequestDTO.getAmbulanceType());
            return ResponseEntity.status(201).body(Map.of("bookingId",bookingId));
        } catch (JsonProcessingException e) {
            throw new AmbulanceCustomException("Failed to create booking",400,e.getMessage());
        }
    }

    @PostMapping("/set-dropoff-location")
    public ResponseEntity<?> setDropOffLocation(@RequestBody DropOffLocationRequestDTO dropOffLocationRequestDTO){

        try {
            ambulanceService.setDropOffLocationOfBooking(dropOffLocationRequestDTO.getBookingId(),dropOffLocationRequestDTO.getLat(),dropOffLocationRequestDTO.getLon());
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            throw new AmbulanceCustomException("Failed to set drop off location",400,e.getMessage());
        }
    }

    @GetMapping("/current-location-ambulance/{bookingId}")
    public ResponseEntity<?> getCurrentLocationOfAmbulance(@PathVariable("bookingId") String bookingId) {
        String data=realTimeTrackingService.getCurrentLocationOfAmbulance(bookingId);
        try {
            RealTimeLocationUpdatesDTO realTimeLocationUpdatesDTO = objectMapper.readValue(data, RealTimeLocationUpdatesDTO.class);
            return ResponseEntity.status(200).body(realTimeLocationUpdatesDTO);
        } catch (Exception e) {
            throw new AmbulanceCustomException("Failed to get current location",400,e.getMessage());
        }
    }

    @ExceptionHandler(AmbulanceCustomException.class)
    public ResponseEntity<?> handleException(HttpServletResponse resp, AmbulanceCustomException exception){
        ErrorResponseDTO errorResponse=new ErrorResponseDTO(exception.getMessage(),exception.getStatus());
        log.error("{}  {}", exception.getMessage(), exception.getExceptionMessage());
        return ResponseEntity.status(errorResponse.getStatus()).body(resp);
    }

}
