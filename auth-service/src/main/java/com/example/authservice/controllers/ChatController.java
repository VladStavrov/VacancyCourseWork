package com.example.authservice.controllers;

import com.example.authservice.DTOs.chat.ChatCreateDTO;
import com.example.authservice.DTOs.chat.ChatDTO;
import com.example.authservice.services.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
//Todo Поменять

    @PostMapping("/create")
    public ResponseEntity<ChatDTO> createChat(@RequestBody ChatCreateDTO chatCreateDTO) {
        ChatDTO chatDTO = chatService.createChat(chatCreateDTO);
        return new ResponseEntity<>(chatDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable Long chatId) {
        ChatDTO chatDTO = chatService.getChatById(chatId);
        return new ResponseEntity<>(chatDTO, HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<ChatDTO>> getAllChatsByUser(@PathVariable String username) {
        List<ChatDTO> chatDTOList = chatService.getAllChatsByUser(username);
        return new ResponseEntity<>(chatDTOList, HttpStatus.OK);
    }
}