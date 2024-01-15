package com.example.authservice.DTOs.auth;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}