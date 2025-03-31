package com.medwell.ambulance.auth;


import com.medwell.ambulance.entity.CustomUser;
import com.medwell.ambulance.repository.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private CustomUserRepository customUserRepository;


    public CustomUser registerNewUser(String name, String email, String mobileNumber, String userType)
    {
        CustomUser user=customUserRepository.findByEmailOrMobileNumber(email,mobileNumber);
        if (user!=null) return user;
        CustomUser customUser=CustomUser.builder()
                .email(email).name(name).mobileNumber(mobileNumber).userType(userType).build();
        return customUserRepository.save(customUser);


    }
}
