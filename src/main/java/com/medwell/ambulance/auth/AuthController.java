package com.medwell.ambulance.auth;


import com.medwell.ambulance.dto.RegisterUserDTO;
import com.medwell.ambulance.entity.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDTO registerUserDTO){

        CustomUser customUser =authService.registerNewUser(registerUserDTO.getName(),registerUserDTO.getEmail(),registerUserDTO.getMobileNumber(),registerUserDTO.getUserType());
        return ResponseEntity.status(200).body(customUser);
    }


}
