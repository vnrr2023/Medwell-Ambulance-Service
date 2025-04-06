package com.medwell.ambulance.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingStatusRequestDTO {

    private String updatedStatus;
    private String bookingId;

}
