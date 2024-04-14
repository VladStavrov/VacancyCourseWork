package com.example.authservice.DTOs.profile.profile;

import com.example.authservice.DTOs.profile.node.NodeDTO;
import lombok.Data;

import java.util.Set;

@Data
public class ProfileCreateDTO {
    private String firstName;
    private String lastName;
    private String jobTitle;
    private String about;
    private Set<NodeDTO> language;
    private Set<NodeDTO> skills;
}