package com.example.authservice.DTOs.profile.node;

import lombok.Data;

import java.util.List;

@Data
public class ApiNodeListDTO {
    List<ApiNodeDto> results;
}