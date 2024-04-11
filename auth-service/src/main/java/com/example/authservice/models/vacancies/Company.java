package com.example.authservice.models.vacancies;

import com.example.authservice.models.auth.Person;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Company {
    private String companyName;
    @Embedded
    private Location location;
    private String description;
    private String email;
    private String phoneNumber;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "person_id", unique = true)
    private Person person;
    @Id
    @GeneratedValue
    private Long id;
}