package com.example.authservice.models.vacancies;
import lombok.Getter;
@Getter
public enum ExperienceLevel {
    NO_FILTER("Не имеет значения"),
    NO_EXPERIENCE("Нет опыта"),
    JUNIOR("От 1 до 3 лет"),
    MIDDLE("От 3 до 5"),
    SENIOR("От 5 лет и более");
    private final String description;
    ExperienceLevel(String description) {
        this.description = description;
    }
}