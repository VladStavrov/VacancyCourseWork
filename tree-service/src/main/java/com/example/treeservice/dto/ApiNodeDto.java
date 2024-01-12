package com.example.treeservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiNodeDto {
    private Long id;
    private String node_type;
    private String title;
    private String slug;
    private String content;
}
