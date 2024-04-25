package com.example.authservice.DTOs.profile.profile;

import com.example.authservice.DTOs.auth.PersonDTO;
import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.DTOs.profile.work.WorkExperienceDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String about;
    private PersonDTO person;
    private Set<NodeDTO> language;
    private Set<NodeDTO> skills;
}