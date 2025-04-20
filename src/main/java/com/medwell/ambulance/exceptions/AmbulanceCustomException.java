package com.medwell.ambulance.exceptions;


import lombok.Getter;

@Getter
public class AmbulanceCustomException extends RuntimeException {

    private String message;
    private int status;
    private String exceptionMessage;

    public AmbulanceCustomException(String message, int status, String exceptionMessage) {
        this.message = message;
        this.status = status;
        this.exceptionMessage = exceptionMessage;
    }
}
