package com.example.authservice.DTOs;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}