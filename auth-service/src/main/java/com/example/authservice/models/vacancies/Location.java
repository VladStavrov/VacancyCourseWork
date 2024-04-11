package com.example.authservice.models.vacancies;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Location {

    private String country;

    private String city;

    private String street;

    private String home;
}
