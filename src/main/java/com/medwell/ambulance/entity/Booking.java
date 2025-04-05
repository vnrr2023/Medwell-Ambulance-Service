package com.medwell.ambulance.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    private String id;

    @PrePersist
    private void assignId(){
        if (this.id==null) this.id= NanoIdUtils.randomNanoId();
    }

    @OneToOne
    @JoinColumn(name = "ambulance_id")
    private CustomUser ambulance;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private CustomUser customer;

    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropLatitude;
    private Double dropLongitude;

    private LocalDateTime requestedAt;

    private LocalDateTime updatedAt;

    private boolean assigned;


}
