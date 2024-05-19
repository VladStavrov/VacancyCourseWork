package com.example.authservice.DTOs.auth;


import com.example.authservice.models.auth.Role;
import lombok.Data;

@Data
public class RegistrationUserDTO {

    private String username;
    private String password;
    private String confirmPassword;
    private Role role;

}