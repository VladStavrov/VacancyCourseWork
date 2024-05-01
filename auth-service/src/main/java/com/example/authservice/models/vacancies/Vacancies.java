package com.example.authservice.models.vacancies;

import com.example.authservice.models.profile.Node;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Vacancies {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "vacancy", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Response> responseList = new ArrayList<>();

    @Embedded
    private Salary salary;
    public void setCompanyDB(Company company){
        company.getVacancies().add(this);
        this.company=company;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
            name = "vacancies_skills",
            joinColumns = @JoinColumn(name = "vacancy_id"),
            inverseJoinColumns = @JoinColumn(name = "node_id")
    )
    private Set<Node> skills = new HashSet<>();


    public void setSkillsDB(Set<Node> nodeList){
        this.skills=nodeList;
        nodeList.forEach(node -> node.getVacanciesSkills().add(this));
    }

    private ExperienceLevel experienceLevel;
    private String description;
    private boolean isParsed;
    private String url;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate creatingTime;

    @PrePersist
    protected void onCreate() {
        creatingTime = LocalDate.now();
    }
}