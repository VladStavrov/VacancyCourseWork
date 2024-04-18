package com.example.authservice.DTOs.company.vacancy;

import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.models.vacancies.ExperienceLevel;
import com.example.authservice.models.vacancies.Salary;
import com.example.authservice.models.vacancies.SortType;
import lombok.Data;

import java.util.Set;

@Data
public class VacancyFilterDTO {

    private SortType sortType;
    private String title;
    private int minSalary;
    private ExperienceLevel experienceLevel;
    private String country;
    private String city;
    private Set<NodeDTO> skills;
}