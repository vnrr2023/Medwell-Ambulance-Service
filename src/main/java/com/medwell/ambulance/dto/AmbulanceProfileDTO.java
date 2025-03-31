package com.medwell.ambulance.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmbulanceProfileDTO {

    private String ambulanceType;
    private String numberPlate;
    private String ambulanceBrandName;
    private String userId;
}
