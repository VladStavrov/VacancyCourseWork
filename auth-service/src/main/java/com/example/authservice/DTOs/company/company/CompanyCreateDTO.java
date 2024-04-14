package com.example.authservice.DTOs.company.company;

import com.example.authservice.models.vacancies.Location;
import lombok.Data;

@Data
public class CompanyCreateDTO {
    private String companyName;
    private Location location;
    private String description;
    private String email;
    private String phoneNumber;
}