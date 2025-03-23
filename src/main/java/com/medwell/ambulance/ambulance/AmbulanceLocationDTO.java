package com.medwell.ambulance.ambulance;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmbulanceLocationDTO {

    String ambulanceId;
//    String ambulanceType; // go-> medium mini-> small xl -> large icu based for critical
    Double latitude,longitude;


}
