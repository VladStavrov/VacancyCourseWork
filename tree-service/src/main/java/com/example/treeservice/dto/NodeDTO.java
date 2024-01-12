package com.example.treeservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NodeDTO {

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nodeType;
    private String title;
    private String slug;
    private String content;

}
