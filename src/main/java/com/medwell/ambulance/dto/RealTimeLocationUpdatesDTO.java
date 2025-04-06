package com.medwell.ambulance.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class RealTimeLocationUpdatesDTO {

    private String bookingId;
    private Double lat,lon;

}
