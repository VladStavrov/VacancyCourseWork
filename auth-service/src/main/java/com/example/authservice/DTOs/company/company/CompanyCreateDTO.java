package com.example.authservice.DTOs.company.company;

import com.example.authservice.models.vacancies.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyCreateDTO {
    private String companyName;
    private Location location;
    private String description;
    private String email;
    private String phoneNumber;
}