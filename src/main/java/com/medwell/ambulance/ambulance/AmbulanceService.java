package com.medwell.ambulance.ambulance;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.medwell.ambulance.dto.BookingResponseDTO;
import com.medwell.ambulance.entity.Ambulance;
import com.medwell.ambulance.entity.Booking;
import com.medwell.ambulance.entity.BookingUpdates;
import com.medwell.ambulance.entity.CustomUser;
import com.medwell.ambulance.enums.Status;
import com.medwell.ambulance.repository.AmbulanceRepository;
import com.medwell.ambulance.repository.BookingRepository;
import com.medwell.ambulance.repository.BookingUpdatesRepository;
import com.medwell.ambulance.repository.CustomUserRepository;
import com.medwell.ambulance.utils.RedisUtility;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AmbulanceService {

    @Autowired
    private RedisUtility redisUtility;

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingUpdatesRepository bookingUpdatesRepository;





    public Ambulance addAmbulanceData(String ambulanceType, String ambulanceBrandName, String numberPlate, String userId) {
        CustomUser customUser = customUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ambulance ambulance = Ambulance.builder()
                .ambulanceType(ambulanceType)
                .ambulanceBrandName(ambulanceBrandName)
                .numberPlate(numberPlate)
                .user(customUser)  // Assign fetched user
                .status("FREE")
                .build();

        return ambulanceRepository.save(ambulance);
    }

    @Transactional
    public BookingResponseDTO acceptBookingRequest(String ambulanceId, String bookingId, String requestId, List<String> otherAmbulances) throws JsonProcessingException {
        Optional<Booking> optionalBooking = bookingRepository.findByIdWithLock(bookingId);

        if (optionalBooking.isEmpty()) {
            return  BookingResponseDTO.builder().status(false).message("No booking found..").build();
        }
        Booking booking = optionalBooking.get();


        if (booking.isAssigned()) {
            return  BookingResponseDTO.builder().status(false).message("Booking already assigned...").build();

        }
        booking.setAssigned(true);
        CustomUser customUser=customUserRepository.findById(ambulanceId).get();
        booking.setAmbulance(customUser);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);
        BookingUpdates updates=bookingUpdatesRepository.findByBooking(booking);
        updates.setStatus(Status.ASSIGNED);
        updates.setUpdatedAt(LocalDateTime.now());
        redisUtility.removeAmbulanceGeoData(ambulanceId);

        redisUtility.removeBooking(bookingId);

        redisUtility.removeRequestsFromAmbulances(requestId,otherAmbulances);

        return BookingResponseDTO.builder().booking(booking).status(true).message("Assignment Successfull")
                .build();


    }
}
