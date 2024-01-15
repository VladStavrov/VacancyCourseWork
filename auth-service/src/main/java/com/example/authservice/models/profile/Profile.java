package com.example.authservice.models.profile;


import com.example.authservice.models.auth.Person;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String jobTitle;

    @Column(columnDefinition = "TEXT")
    private String about;

    @OneToOne
    @JoinColumn(name = "person_id", unique = true)
    private Person person;

    @ManyToMany
    @JoinTable(
            name = "profile_knowledges",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "node_id")
    )
    private Set<Node> knowledge = new HashSet<>();

    @OneToMany(mappedBy = "profile")
    private List<WorkExperience> workExperiences = new ArrayList<>();

}
