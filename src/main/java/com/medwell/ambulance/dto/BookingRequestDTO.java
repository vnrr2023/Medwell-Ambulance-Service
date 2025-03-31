package com.medwell.ambulance.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDTO {

    private String customerId;
    private Double lat;
    private Double lon;
    private String ambulanceType;

}
