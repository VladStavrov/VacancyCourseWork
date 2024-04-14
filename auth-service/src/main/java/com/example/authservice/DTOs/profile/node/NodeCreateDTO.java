package com.example.authservice.DTOs.profile.node;

import lombok.Data;

@Data
public class NodeCreateDTO {
    private String nodeType;
    private String title;
    private String slug;
    private String content;
}