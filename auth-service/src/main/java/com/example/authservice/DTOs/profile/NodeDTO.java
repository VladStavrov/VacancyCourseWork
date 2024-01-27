package com.example.authservice.DTOs.profile;

import lombok.Data;

@Data
public class NodeDTO {
    private Long id;
    private String nodeType;
    private String title;
    private String slug;
    private String content;
}
