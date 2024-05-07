package com.example.authservice.DTOs.chat;

import com.example.authservice.models.chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCreateDTO {
    private String company;
    private String person;
}
