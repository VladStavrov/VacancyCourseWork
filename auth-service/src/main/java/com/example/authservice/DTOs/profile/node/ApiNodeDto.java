package com.example.authservice.DTOs.profile.node;

import lombok.Data;

@Data
public class ApiNodeDto {
    private Long id;
    private String node_type;
    private String title;
    private String slug;
    private String content;
}