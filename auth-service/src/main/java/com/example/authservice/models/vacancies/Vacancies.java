package com.example.authservice.models.vacancies;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Vacancies {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private Company company;


    @Embedded
    private Salary salary;

    private ExperienceLevel experienceLevel;
    private String description;
    private boolean isParsed;
    private String url;
}