package com.example.authservice.requests;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}