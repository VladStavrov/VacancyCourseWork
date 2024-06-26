package com.example.authservice.models.profile;


import com.example.authservice.models.auth.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String company;
    private String jobTitle;
    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateStart;


    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateFinish;


    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;


    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
            name = "work_experience_primary_skills",
            joinColumns = @JoinColumn(name = "work_experience_id"),
            inverseJoinColumns = @JoinColumn(name = "node_id")
    )
    private Set<Node> primarySkills = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
            name = "work_experience_secondary_skills",
            joinColumns = @JoinColumn(name = "work_experience_id"),
            inverseJoinColumns = @JoinColumn(name = "node_id")
    )
    private Set<Node> secondarySkills = new HashSet<>();


    public void setPersonDB(Person person){
        this.person=person;
        person.getWorkExperienceList().add(this);
    }
    public void setSecondarySkillsDB(Set<Node> nodeList){
        this.secondarySkills=nodeList;
        nodeList.forEach(node -> node.getSecondarySkillExperiences().add(this));
    }
    public void setPrimarySkillsDB(Set<Node> nodeList){
        this.primarySkills=nodeList;
        nodeList.forEach(node -> node.getPrimarySkillExperiences().add(this));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkExperience that = (WorkExperience) o;
        return Objects.equals(id, that.id) && Objects.equals(company, that.company) && Objects.equals(jobTitle, that.jobTitle) && Objects.equals(dateStart, that.dateStart) && Objects.equals(dateFinish, that.dateFinish) && Objects.equals(description, that.description)  && Objects.equals(primarySkills, that.primarySkills) && Objects.equals(secondarySkills, that.secondarySkills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, company, jobTitle, dateStart, dateFinish, description);
    }


}
