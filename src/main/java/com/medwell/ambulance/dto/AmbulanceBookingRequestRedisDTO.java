package com.medwell.ambulance.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AmbulanceBookingRequestRedisDTO {

    private Double pickupLat;
    private Double pickupLon;
    private String bookingId;
    private List<String> otherAmbulances;
    private String requestId;
    private String distance;
}
