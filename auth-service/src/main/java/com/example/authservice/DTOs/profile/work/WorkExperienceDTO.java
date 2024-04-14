package com.example.authservice.DTOs.profile.work;

import com.example.authservice.DTOs.profile.node.NodeDTO;
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
    private Long profileId;
}