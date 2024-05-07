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
public class ChatDTO {
    private String company;
    private String person;
    private String companyName;
    private String personData;
    private Long id;
    private List<MessageDTO> messages;
    private int procent;

    public ChatDTO(Chat chat){
        this.company = chat.getCompany().getUsername();
        this.person = chat.getPerson().getUsername();
        this.companyName = chat.getCompany().getCompany().getCompanyName();
        this.personData = chat.getPerson().getProfile().getFirstName() + " " + chat.getPerson().getProfile().getLastName();
        this.id = chat.getId();
        this.messages = chat.getMessageList().stream()
                .map(MessageDTO::new).collect(Collectors.toList());
    }
}