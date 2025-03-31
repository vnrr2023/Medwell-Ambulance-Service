package com.medwell.ambulance.repository;

import com.medwell.ambulance.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomUserRepository extends JpaRepository<CustomUser, String> {

    CustomUser findByEmailOrMobileNumber(String email,String mobile);

}