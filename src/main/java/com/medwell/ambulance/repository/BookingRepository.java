package com.medwell.ambulance.repository;

import com.medwell.ambulance.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, String> {
}