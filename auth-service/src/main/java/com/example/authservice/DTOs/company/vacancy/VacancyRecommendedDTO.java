package com.example.authservice.DTOs.company.vacancy;

import com.example.authservice.models.vacancies.Vacancies;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyRecommendedDTO {
    private VacancyDTO vacancy;
    private double matchingPercentage;
    public VacancyRecommendedDTO(Vacancies vacancy,double matchingPercentage) {
        this.vacancy = new VacancyDTO(vacancy);
        this.matchingPercentage = matchingPercentage;
    }
}
