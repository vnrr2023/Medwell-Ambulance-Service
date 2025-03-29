package com.medwell.ambulance.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.medwell.ambulance.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingUpdates {

    @Id
    private String id;

    @PrePersist
    private void assignId(){
        if (this.id!=null) this.id= NanoIdUtils.randomNanoId();
    }

    @Enumerated(EnumType.STRING)
    private Status status; // REQUESTED ; ASSIGNED ; IN_TRANSIT ; COMPLETED ; ARRIVING

    @ManyToOne
    private Booking booking;

    private LocalDateTime updatedAt;


}
