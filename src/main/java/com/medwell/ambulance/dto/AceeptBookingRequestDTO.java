package com.medwell.ambulance.dto;

import lombok.Data;

import java.util.List;

@Data
public class AceeptBookingRequestDTO {

    private String ambulanceId;
    private String bookingId;
    private String requestId;
    private List<String> otherAmbulances;
}
