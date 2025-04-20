package com.medwell.ambulance.ambulance;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.medwell.ambulance.dto.BookingResponseDTO;
import com.medwell.ambulance.dto.BookingStatusRequestDTO;
import com.medwell.ambulance.entity.Ambulance;
import com.medwell.ambulance.entity.Booking;
import com.medwell.ambulance.entity.BookingUpdates;
import com.medwell.ambulance.entity.CustomUser;
import com.medwell.ambulance.enums.Status;
import com.medwell.ambulance.notification.NotificationSenderService;
import com.medwell.ambulance.repository.AmbulanceRepository;
import com.medwell.ambulance.repository.BookingRepository;
import com.medwell.ambulance.repository.BookingUpdatesRepository;
import com.medwell.ambulance.repository.CustomUserRepository;
import com.medwell.ambulance.utils.GmapsService;
import com.medwell.ambulance.utils.RedisBookingService;
import com.medwell.ambulance.utils.RedisGeoLocationService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AmbulanceService {

    @Autowired
    private RedisGeoLocationService redisGeoLocationService;

    @Autowired
    private RedisBookingService redisBookingService;

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingUpdatesRepository bookingUpdatesRepository;


    @Autowired
    private GmapsService gmapsService;

    @Autowired
    private NotificationSenderService notificationSenderService;



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
    public BookingResponseDTO acceptBookingRequest(String ambulanceId, String bookingId, String requestId, List<String> otherAmbulances,Double driverLat,Double driverLon) throws JsonProcessingException {
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
        String polyine=gmapsService.getPolyline(driverLat,driverLon,booking.getPickupLatitude(),booking.getPickupLongitude());
        booking.setRouteToCustomer(polyine);
        String linkToCustomer= String.format(
                "https://www.google.com/maps/dir/?api=1&destination=%.14f,%.14f&travelmode=driving",
                booking.getPickupLatitude(),booking.getPickupLongitude()
        );
        booking.setPickUpLink(linkToCustomer);
        bookingRepository.save(booking);

        BookingUpdates updates=new BookingUpdates();
        updates.setStatus(Status.ASSIGNED);
        updates.setUpdatedAt(LocalDateTime.now());
        updates.setBooking(booking);
        bookingUpdatesRepository.save(updates);

        redisGeoLocationService.removeAmbulanceGeoData(ambulanceId);

        redisBookingService.removeBooking(bookingId);

        redisBookingService.removeRequestsFromAmbulances(requestId,otherAmbulances);
        try {
            notificationSenderService.sendStatusUpdateNotification(
                    booking.getCustomer().getId(), Status.ASSIGNED
            );
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send notification {}", e.getMessage());
        }
        return BookingResponseDTO.builder().booking(booking).status(true).message("Assignment Successfull")
                .build();

    }

    public void updateBookingStatusOfLocation(String updatedStatus,String bookingId){
        Booking booking=bookingRepository.findById(bookingId).get();
        BookingUpdates bookingUpdates=new BookingUpdates();
        Status status=switch (updatedStatus){
            case "ARRIVED" -> Status.ARRIVED;
            case "COMPLETED" -> Status.COMPLETED;
            case "IN_TRANSIT" -> Status.IN_TRANSIT;
            default -> Status.COMPLETED;
        };
        bookingUpdates.setStatus(status);
        bookingUpdates.setUpdatedAt(LocalDateTime.now());
        bookingUpdates.setBooking(booking);
        bookingUpdatesRepository.save(bookingUpdates);
        try {
            notificationSenderService.sendStatusUpdateNotification(booking.getCustomer().getId(), status);
        }
        catch (FirebaseMessagingException e){
            log.error("Failed to send updates notification {}", e.getMessage());
        }

    }


    public String setDropOffLocationOfBooking(String bookingId, Double lat, Double lon) {

        Booking booking=bookingRepository.findById(bookingId).get();
        booking.setDropLongitude(lon);
        booking.setDropLatitude(lat);
        String linkToDestination= String.format(
                "https://www.google.com/maps/dir/?api=1&destination=%.14f,%.14f&travelmode=driving",
                lat, lon
        );
        booking.setDropOffLink(linkToDestination);
        bookingRepository.save(booking);

        return linkToDestination;

    }
}
