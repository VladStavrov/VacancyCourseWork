package com.example.authservice.DTOs.company.vacancy;

import com.example.authservice.models.vacancies.ExperienceLevel;
import com.example.authservice.models.vacancies.Salary;
import lombok.Data;

@Data
public class VacancyCreateDTO {
    private String title;
    private Long companyId;
    private Salary salary;
    private ExperienceLevel experienceLevel;
    private String description;
    private boolean isParsed;
    private String url;
}