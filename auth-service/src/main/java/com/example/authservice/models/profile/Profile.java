package com.example.authservice.models.profile;


import com.example.authservice.models.auth.Person;
import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

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

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "person_id", unique = true)
    private Person person;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
            name = "profile_knowledges",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "node_id")
    )
    private Set<Node> knowledge = new HashSet<>();

    @OneToMany(mappedBy = "profile",cascade = CascadeType.ALL)
    private List<WorkExperience> workExperiences = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id) && Objects.equals(firstName, profile.firstName) && Objects.equals(lastName, profile.lastName) && Objects.equals(jobTitle, profile.jobTitle) && Objects.equals(about, profile.about) && Objects.equals(person, profile.person) && Objects.equals(knowledge, profile.knowledge) && Objects.equals(workExperiences, profile.workExperiences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, jobTitle, about);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}
