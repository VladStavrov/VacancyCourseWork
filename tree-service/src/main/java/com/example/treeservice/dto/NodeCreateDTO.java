package com.example.treeservice.dto;

import lombok.Data;

@Data
public class NodeCreateDTO {
    private String nodeType;
    private String title;
    private String slug;
    private String content;


}