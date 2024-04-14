package com.example.authservice.DTOs.company.vacancy;

import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.models.vacancies.ExperienceLevel;
import com.example.authservice.models.vacancies.Salary;
import lombok.Data;

import java.util.Set;

@Data
public class VacancyCreateDTO {
    private String title;
    private Long companyId;
    private Salary salary;
    private ExperienceLevel experienceLevel;
    private String description;
    private boolean isParsed;
    private String url;
    private Set<NodeDTO> skills;
}