package com.medwell.ambulance.auth;


import com.medwell.ambulance.dto.RegisterUserDTO;
import com.medwell.ambulance.entity.CustomUser;
import com.medwell.ambulance.exceptions.AmbulanceCustomException;
import com.medwell.ambulance.exceptions.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserDTO registerUserDTO){
        try {
            CustomUser customUser =authService.registerNewUser(registerUserDTO.getName(),registerUserDTO.getEmail(),registerUserDTO.getMobileNumber(),registerUserDTO.getUserType());
            return ResponseEntity.status(200).body(customUser);
        } catch (Exception e) {
            throw new AmbulanceCustomException("Failed to register",400,e.getMessage());
        }
    }

    @ExceptionHandler(AmbulanceCustomException.class)
    public ResponseEntity<?> handleException(HttpServletResponse resp, AmbulanceCustomException exception){
        ErrorResponseDTO errorResponse=new ErrorResponseDTO(exception.getMessage(),exception.getStatus());
        log.error("{}  {}", exception.getMessage(), exception.getExceptionMessage());
        return ResponseEntity.status(errorResponse.getStatus()).body(resp);
    }


}
