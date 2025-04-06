package com.medwell.ambulance.dto;

import lombok.Data;

@Data
public class DropOffLocationRequestDTO {

    private Double lat;
    private Double lon;
    private String bookingId;

}
