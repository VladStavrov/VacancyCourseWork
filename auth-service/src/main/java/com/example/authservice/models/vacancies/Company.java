package com.example.authservice.models.vacancies;

import com.example.authservice.models.auth.Person;
import com.example.authservice.models.profile.WorkExperience;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = {"companyName","id"})
public class Company {
    @Column(unique = true)
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

    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Vacancies> vacancies= new ArrayList<>();

}