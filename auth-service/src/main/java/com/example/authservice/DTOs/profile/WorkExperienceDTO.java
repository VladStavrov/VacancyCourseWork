package com.example.authservice.DTOs.profile;

import com.example.authservice.models.profile.Node;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class WorkExperienceDTO {

    private Long id;
    private String company;
    private String jobTitle;
    private LocalDate dateStart;
    private LocalDate dateFinish;
    private String description;
    private Set<NodeDTO> primarySkills;
    private Set<NodeDTO> secondarySkills;

}