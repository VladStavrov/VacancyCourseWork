package com.example.authservice.models.vacancies;

import lombok.Data;


public enum ExperienceLevel {
    NO_EXPERIENCE("Нет опыта"),
    JUNIOR("От 1 до 3 лет"),
    MIDDLE("От 3 до 5"),
    SENIOR("От 5 лет и более");

    private final String description;

    ExperienceLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}