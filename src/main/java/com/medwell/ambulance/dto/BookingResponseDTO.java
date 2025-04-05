package com.medwell.ambulance.dto;

import com.medwell.ambulance.entity.Booking;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponseDTO {

    private Booking booking;
    private boolean status;
    private String message;

}
