package com.medwell.ambulance.repository;

import com.medwell.ambulance.entity.BookingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingPaymentRepository extends JpaRepository<BookingPayment, String> {
}