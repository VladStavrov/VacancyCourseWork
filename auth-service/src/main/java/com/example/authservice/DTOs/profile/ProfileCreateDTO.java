package com.example.authservice.DTOs.profile;

import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
public class ProfileCreateDTO {
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String about;
    private Set<NodeDTO> knowledge;
    private List<WorkExperienceDTO> workExperiences;
}