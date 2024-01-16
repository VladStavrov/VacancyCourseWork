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

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.REFRESH)
    private Set<Profile> skillsProfils = new HashSet<>();

    @ManyToMany(mappedBy = "language", cascade = CascadeType.REFRESH)
    private Set<Profile> languageProfiles = new HashSet<>();

    @ManyToMany(mappedBy = "primarySkills", cascade = CascadeType.REFRESH)
    private Set<WorkExperience> primarySkillExperiences = new HashSet<>();

    @ManyToMany(mappedBy = "secondarySkills", cascade = CascadeType.REFRESH)
    private Set<WorkExperience> secondarySkillExperiences = new HashSet<>();

    @PreRemove
    private void removeNodeAssociations() {
        for(Profile profile : this.languageProfiles){
            profile.getLanguage().remove(this);
        }
        for(Profile profile : this.skillsProfils){
            profile.getSkills().remove(this);
        }
        for (WorkExperience workExperience : this.primarySkillExperiences) {
            workExperience.getPrimarySkills().remove(this);
        }

        for (WorkExperience workExperience : this.secondarySkillExperiences) {
            workExperience.getSecondarySkills().remove(this);
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