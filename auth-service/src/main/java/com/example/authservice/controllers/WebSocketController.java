package com.example.authservice.controllers;



import com.example.authservice.DTOs.chat.MessageCreateDTO;
import com.example.authservice.DTOs.chat.MessageDTO;
import com.example.authservice.services.chat.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller("/chating")
@RequiredArgsConstructor
public class WebSocketController {
    private final MessageService messageService;
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public MessageDTO chat(@DestinationVariable Long roomId, MessageCreateDTO message) {
        return messageService.createMessage(message, roomId);
    }
}