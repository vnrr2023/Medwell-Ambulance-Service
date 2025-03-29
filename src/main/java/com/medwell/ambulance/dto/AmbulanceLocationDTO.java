package com.medwell.ambulance.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmbulanceLocationDTO {

    private String ambulanceId;
//    String ambulanceType; // go-> medium mini-> small xl -> large icu based for critical
    private Double latitude,longitude;


}
