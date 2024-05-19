package com.example.authservice.services.chat;


import com.example.authservice.DTOs.chat.ChatCreateDTO;
import com.example.authservice.DTOs.chat.ChatDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.models.chat.Chat;
import com.example.authservice.models.chat.Message;
import com.example.authservice.repositories.chat.ChatRepository;
import com.example.authservice.services.auth.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final PersonService personService;
    public ChatDTO createChat(ChatCreateDTO chatCreateDTO) {
        Optional<Chat> optionalChat = chatRepository.findByCompanyUsernameAndPersonUsername(chatCreateDTO.getCompany(), chatCreateDTO.getPerson());
         if (optionalChat.isPresent()) {
            return new ChatDTO(optionalChat.get());
        } else {
            Person company = personService.findByUsername(chatCreateDTO.getCompany());
            Person person = personService.findByUsername(chatCreateDTO.getPerson());
            if(company.getCompany()==null || person.getProfile()==null){
               throw new ResponseStatusException(HttpStatus.CONFLICT, "Не удалось создать чат");
            }
            Chat chat = new Chat();
            chat.setCompany(company);
            chat.setPerson(person);
            chat = chatRepository.save(chat);
            return new ChatDTO(chat);
        }
    }
    public ChatDTO getChatById(Long chatId) {
         Chat optionalChat = chatRepository.findByIdOrderByMessageListCreatingTimeAsc(chatId);
         return new ChatDTO(optionalChat);
    }
    public Chat getChatByIdReal(Long chatId) {
        Chat optionalChat = chatRepository.findByIdOrderByMessageListCreatingTimeAsc(chatId);
        return optionalChat;
    }
    public List<ChatDTO> getAllChatsByUser(String username) {
        List<Chat> chats = chatRepository.findByCompanyUsernameOrPersonUsername(username, username);
        return chats.stream()
                .filter(chat -> !chat.getMessageList().isEmpty())
                .sorted(Comparator.comparing(chat -> chat.getMessageList().stream()
                                .map(Message::getCreatingTime)
                                .max(Comparator.naturalOrder())
                                .orElse(null),
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(ChatDTO::new)
                .collect(Collectors.toList());
    }
}
