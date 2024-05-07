package com.example.authservice.DTOs.company.response;

import com.example.authservice.DTOs.company.vacancy.VacancyDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCreateDTO {
    private String username;
    private Long vacancyId;
    private String status;
}