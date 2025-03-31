package com.medwell.ambulance.entity;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ambulance {

    @Id
    private String id;

    private String ambulanceType;

    private String numberPlate;

    private String ambulanceBrandName;

    @OneToOne
    private CustomUser user;

    private String status;


    @PrePersist
    private void assignId(){
        if (this.id==null) this.id= NanoIdUtils.randomNanoId();
    }



}
