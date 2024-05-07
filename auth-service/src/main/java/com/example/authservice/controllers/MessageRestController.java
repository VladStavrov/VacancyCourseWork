package com.example.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.authservice.DTOs.chat.MessageDTO;
import com.example.authservice.services.chat.MessageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageDTO>> getAllMessagesByChatId(@PathVariable Long chatId) {
        List<MessageDTO> messageDTOList = messageService.getAllMessagesByChatId(chatId);
        return new ResponseEntity<>(messageDTOList, HttpStatus.OK);
    }
}