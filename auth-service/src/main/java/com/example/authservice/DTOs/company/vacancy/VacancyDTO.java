package com.example.authservice.DTOs.company.vacancy;

import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.models.vacancies.Company;
import com.example.authservice.models.vacancies.ExperienceLevel;
import com.example.authservice.models.vacancies.Salary;
import com.example.authservice.models.vacancies.Vacancies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDTO {
    private Long id;
    private String title;
    private Long companyId;
    private String companyName;
    private Salary salary;
    private ExperienceLevel experienceLevel;
    private String description;
    private boolean isParsed;
    private String url;
    private List<NodeDTO> skills;
    private LocalDate creatingTime;
    private String country;
    private String city;
    public VacancyDTO(Vacancies vacancy) {
        this.id = vacancy.getId();
        this.title = vacancy.getTitle();
        if (vacancy.getCompany() != null) {
            this.companyId = vacancy.getCompany().getId();
            this.companyName = vacancy.getCompany().getCompanyName();
            this.city = vacancy.getCompany().getLocation().getCity();
            this.country = vacancy.getCompany().getLocation().getCountry();
        }
        this.creatingTime = vacancy.getCreatingTime();
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
