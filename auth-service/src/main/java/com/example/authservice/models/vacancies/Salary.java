package com.example.authservice.models.vacancies;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Salary {
    private int minSalary;
    private int maxSalary;
}
