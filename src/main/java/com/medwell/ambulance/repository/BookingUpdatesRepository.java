package com.medwell.ambulance.repository;

import com.medwell.ambulance.entity.Booking;
import com.medwell.ambulance.entity.BookingUpdates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingUpdatesRepository extends JpaRepository<BookingUpdates, String> {

   BookingUpdates findByBooking(Booking booking);
}