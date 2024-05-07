package com.example.authservice.DTOs.chat;

import com.example.authservice.models.chat.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String message;
    private String username;
    private LocalDateTime creatingTime;
    private Long chatId;
    public MessageDTO(Message message){
        this.message = message.getMessage();
        this.username = message.getPerson().getUsername();
        this.id = message.getId();
        this.chatId = message.getChat().getId();
        this.creatingTime = message.getCreatingTime();
    }
}