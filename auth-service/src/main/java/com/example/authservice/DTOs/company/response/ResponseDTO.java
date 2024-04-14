package com.example.authservice.DTOs.company.response;

import lombok.Data;

@Data
public class ResponseDTO {
    private Long id;
    private String username;
    private Long vacancyId;
    private String status;
}
