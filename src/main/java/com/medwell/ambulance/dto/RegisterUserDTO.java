package com.medwell.ambulance.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String name;
    private String mobileNumber;
    private String email;
    private String userType;

}
