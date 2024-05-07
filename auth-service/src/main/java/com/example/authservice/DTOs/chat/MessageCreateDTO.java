package com.example.authservice.DTOs.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateDTO {
    private String message;
    private String username;
    private Long chatId;
}
