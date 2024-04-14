package com.example.authservice.models.vacancies;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vacancies {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Embedded
    private Salary salary;

    private ExperienceLevel experienceLevel;
    private String description;
    private boolean isParsed;
    private String url;
}