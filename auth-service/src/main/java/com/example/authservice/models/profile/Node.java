package com.example.authservice.models.profile;


import com.example.authservice.models.vacancies.Vacancies;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Node {
    @jakarta.persistence.Id
    @org.springframework.data.annotation.Id
    @SequenceGenerator(name = "node_seq",
            sequenceName = "node_sequence",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "node_seq")
    private Long id;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String nodeType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.REFRESH)
    private Set<Profile> skillsProfils = new HashSet<>();

    @ManyToMany(mappedBy = "primarySkills", cascade = CascadeType.REFRESH)
    private Set<WorkExperience> primarySkillExperiences = new HashSet<>();

    @ManyToMany(mappedBy = "secondarySkills", cascade = CascadeType.REFRESH)
    private Set<WorkExperience> secondarySkillExperiences = new HashSet<>();

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.REFRESH)
    private Set<Vacancies> vacanciesSkills = new HashSet<>();

    @PreRemove
    private void removeNodeAssociations() {

        for(Profile profile : this.skillsProfils){
            profile.getSkills().remove(this);
        }
        for (WorkExperience workExperience : this.primarySkillExperiences) {
            workExperience.getPrimarySkills().remove(this);
        }
        for (WorkExperience workExperience : this.secondarySkillExperiences) {
            workExperience.getSecondarySkills().remove(this);
        }
        for (Vacancies vacancies : this.vacanciesSkills) {
            vacancies.getSkills().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id) && Objects.equals(title, node.title) && Objects.equals(slug, node.slug) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(slug);
    }
    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}