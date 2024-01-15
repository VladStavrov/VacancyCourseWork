package com.example.authservice.models.profile;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Node {
    @Id
    @SequenceGenerator(name = "node_seq2",
            sequenceName = "node_sequence2",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "node_seq2")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @ManyToMany(mappedBy = "knowledge", cascade = CascadeType.ALL)
    private Set<Profile> profiles = new HashSet<>();

    @ManyToMany(mappedBy = "primarySkills")
    private Set<WorkExperience> primarySkillExperiences = new HashSet<>();

    @ManyToMany(mappedBy = "secondarySkills")
    private Set<WorkExperience> secondarySkillExperiences = new HashSet<>();

    @PreRemove
    private void removeNodeAssociations() {
        for(Profile profile : this.profiles){
            profile.getKnowledge().remove(this);
        }
        for (WorkExperience workExperience : this.primarySkillExperiences) {
            workExperience.getPrimarySkills().remove(this);
        }
        System.out.println("ДО: "+ this.secondarySkillExperiences);
        for (WorkExperience workExperience : this.secondarySkillExperiences) {
            System.out.println("WE: "+workExperience.getSecondarySkills());
            workExperience.getSecondarySkills().remove(this);
        }
        System.out.println("После: "+ this.secondarySkillExperiences);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id) && Objects.equals(title, node.title) && Objects.equals(slug, node.slug) && Objects.equals(profiles, node.profiles) && Objects.equals(primarySkillExperiences, node.primarySkillExperiences) && Objects.equals(secondarySkillExperiences, node.secondarySkillExperiences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slug); // Где slug - поле, участвующее в вычислении хеш-кода
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