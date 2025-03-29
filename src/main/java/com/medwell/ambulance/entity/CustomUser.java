package com.medwell.ambulance.entity;


import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUser {

    @Id
    private String id;

    private String name;

    private String mobileNumber;

    private String email;

    private String userType;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void assignId(){
        if (this.id!=null) this.id= NanoIdUtils.randomNanoId();
    }

}
