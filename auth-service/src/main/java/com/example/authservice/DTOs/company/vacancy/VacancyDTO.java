package com.example.authservice.DTOs.company.vacancy;

import com.example.authservice.models.vacancies.Company;
import com.example.authservice.models.vacancies.ExperienceLevel;
import com.example.authservice.models.vacancies.Salary;
import com.example.authservice.models.vacancies.Vacancies;
import lombok.Data;

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
    }
}
