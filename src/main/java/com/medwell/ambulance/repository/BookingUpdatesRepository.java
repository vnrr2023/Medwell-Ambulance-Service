package com.medwell.ambulance.repository;

import com.medwell.ambulance.entity.BookingUpdates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingUpdatesRepository extends JpaRepository<BookingUpdates, String> {
}