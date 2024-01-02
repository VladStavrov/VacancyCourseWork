package com.example.authservice.controllers;



import com.example.authservice.DTOs.RegistrationUserDTO;
import com.example.authservice.requests.JwtRequest;
import com.example.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/identity")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
       return authService.createAuthToken(authRequest);
    }
    @PostMapping("/registration")
    public ResponseEntity<?> createNewPerson(@RequestBody RegistrationUserDTO registrationUserDTO)  {
       return ResponseEntity.ok(authService.createNewPerson(registrationUserDTO));
    }

}