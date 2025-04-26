package com.medwell.ambulance.customer;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medwell.ambulance.dto.AmbulanceBookingRequestRedisDTO;
import com.medwell.ambulance.entity.Booking;
import com.medwell.ambulance.entity.BookingUpdates;
import com.medwell.ambulance.entity.CustomUser;
import com.medwell.ambulance.enums.Status;
import com.medwell.ambulance.repository.BookingRepository;
import com.medwell.ambulance.repository.BookingUpdatesRepository;
import com.medwell.ambulance.repository.CustomUserRepository;
import com.medwell.ambulance.utils.RedisBookingService;
import com.medwell.ambulance.utils.RedisGeoLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private RedisGeoLocationService redisGeoLocationService;

    @Autowired
    private RedisBookingService redisBookingService;

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingUpdatesRepository bookingUpdatesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public String createBooking(String customerId, Double lat, Double lon, String ambulanceType) throws JsonProcessingException {
        LocalDateTime localDateTime=LocalDateTime.now();
        CustomUser customer=customUserRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking= Booking.builder()
                .customer(customer)
                .pickupLatitude(lat)
                .pickupLongitude(lon)
                .updatedAt(localDateTime)
                .requestedAt(localDateTime)
                .build();
        bookingRepository.save(booking);
        BookingUpdates bookingUpdates=BookingUpdates.builder()
                .booking(booking)
                .updatedAt(localDateTime)
                .status(Status.REQUESTED)
                .build();
        bookingUpdatesRepository.save(bookingUpdates);
        redisBookingService.setBooking(booking.getId());
        List<Map<String,String>> ambulanceData= redisGeoLocationService.getNearbyAmbulancesByType(ambulanceType,lat,lon);
        List<String> ambulanceIds = ambulanceData.stream()
                .map(map -> map.get("ambulanceId"))
                .collect(Collectors.toList());
        AmbulanceBookingRequestRedisDTO bookingRequestRedisDTO=AmbulanceBookingRequestRedisDTO.builder()
                                    .bookingId(booking.getId())
                                    .pickupLat(lat).pickupLon(lon)
                                    .otherAmbulances(ambulanceIds)
                                    .requestId(NanoIdUtils.randomNanoId())
                                    .build();

        for(Map<String,String> ambulance:ambulanceData){
            bookingRequestRedisDTO.setDistance(ambulance.get("distance"));
            String request=objectMapper.writeValueAsString(bookingRequestRedisDTO);
            redisBookingService.setAmbulanceRequest(request,ambulance.get("ambulanceId"));

        }

        return booking.getId();

    }

    public Booking getBookingDetails(String bookingId) {
        Booking booking=bookingRepository.findById(bookingId).get();
        return booking;
    }

}
