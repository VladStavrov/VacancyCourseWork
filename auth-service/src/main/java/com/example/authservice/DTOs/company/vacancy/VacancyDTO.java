package com.example.authservice.DTOs.company.vacancy;

import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.models.vacancies.Company;
import com.example.authservice.models.vacancies.ExperienceLevel;
import com.example.authservice.models.vacancies.Salary;
import com.example.authservice.models.vacancies.Vacancies;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class VacancyDTO {
    private Long id;
    private String title;
    private Long companyId;
    private Salary salary;
    private ExperienceLevel experienceLevel;
    private String description;
    private boolean isParsed;
    private String url;
    private List<NodeDTO> skills;

    public VacancyDTO(Vacancies vacancy) {
        this.id = vacancy.getId();
        this.title = vacancy.getTitle();
        if (vacancy.getCompany() != null) {
            this.companyId = vacancy.getCompany().getId();
        }
        this.salary = vacancy.getSalary();
        this.experienceLevel = vacancy.getExperienceLevel();
        this.description = vacancy.getDescription();
        this.isParsed = vacancy.isParsed();
        this.url = vacancy.getUrl();
        this.skills = vacancy.getSkills().stream()
                .map(NodeDTO::new)
                .collect(Collectors.toList());
    }
}
