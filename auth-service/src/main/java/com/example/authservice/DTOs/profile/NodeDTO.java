package com.example.authservice.DTOs.profile;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class NodeDTO {
    private Long id;
    private String title;
    private String slug;
}