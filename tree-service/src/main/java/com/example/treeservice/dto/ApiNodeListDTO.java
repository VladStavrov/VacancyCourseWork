package com.example.treeservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiNodeListDTO {
    List<ApiNodeDto> results;
}