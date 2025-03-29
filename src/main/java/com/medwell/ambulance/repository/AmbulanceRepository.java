package com.medwell.ambulance.repository;

import com.medwell.ambulance.entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceRepository extends JpaRepository<Ambulance, String> {
}