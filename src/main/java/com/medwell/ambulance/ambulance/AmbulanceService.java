package com.medwell.ambulance.ambulance;


import com.medwell.ambulance.entity.Ambulance;
import com.medwell.ambulance.entity.CustomUser;
import com.medwell.ambulance.repository.AmbulanceRepository;
import com.medwell.ambulance.repository.CustomUserRepository;
import com.medwell.ambulance.utils.RedisUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmbulanceService {

    @Autowired
    private RedisUtility redisUtility;

    @Autowired
    private CustomUserRepository customUserRepository;

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    public void removeAmbulance(String ambulanceId){
        redisUtility.removeAmbulanceGeoData(ambulanceId);
    }


    public Ambulance addAmbulanceData(String ambulanceType, String ambulanceBrandName, String numberPlate, String userId) {
        CustomUser customUser = customUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ambulance ambulance = Ambulance.builder()
                .ambulanceType(ambulanceType)
                .ambulanceBrandName(ambulanceBrandName)
                .numberPlate(numberPlate)
                .user(customUser)  // Assign fetched user
                .status("FREE")
                .build();

        return ambulanceRepository.save(ambulance);
    }
}
