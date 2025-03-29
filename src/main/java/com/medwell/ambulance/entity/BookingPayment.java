package com.medwell.ambulance.entity;


import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.*;

import java.awt.print.Book;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingPayment {

    @Id
    private String id;

    @PrePersist
    private void assignId(){
        if (this.id!=null) this.id= NanoIdUtils.randomNanoId();
    }

    private boolean paymentStatus=false; // true if done else false

    private double fare;

    private String paymentType;  // UPI  CASH  BANKING

    @OneToOne
    private Booking booking;

}
