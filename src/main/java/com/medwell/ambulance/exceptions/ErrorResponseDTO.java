package com.medwell.ambulance.exceptions;

import lombok.Data;

@Data
public class ErrorResponseDTO {

    private String message;
    private int status;

    public ErrorResponseDTO(String message,int status) {
        this.message = message;
        this.status = status;
    }
}
