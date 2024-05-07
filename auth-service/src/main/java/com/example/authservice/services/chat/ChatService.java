package com.example.authservice.services.chat;


import com.example.authservice.DTOs.chat.ChatCreateDTO;
import com.example.authservice.DTOs.chat.ChatDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.models.chat.Chat;
import com.example.authservice.models.chat.Message;
import com.example.authservice.repositories.chat.ChatRepository;
import com.example.authservice.services.auth.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    /*public ChatDTO createChatIfNotExist(String username1, String username2) {

        Optional<Chat> optionalChat1 = chatRepository.findByPerson1UsernameAndPerson2Username(username1, username2);
        Optional<Chat> optionalChat2 = chatRepository.findByPerson1UsernameAndPerson2Username(username2, username1);

        Optional<Chat> optionalChat = optionalChat1.or(() -> optionalChat2);
        if (optionalChat.isPresent()) {
            return new ChatDTO(optionalChat.get());
        } else {
            // Если чат не существует, создаем новый
            Person person1 = personService.findByUsername(username1);
            Person person2 = personService.findByUsername(username2);
            Chat chat = new Chat();
            chat.setCompany(person1);
            chat.setPerson(person2);
            chat = chatRepository.save(chat);
            return new ChatDTO(chat);
        }
    }*/
    public List<ChatDTO> getAllChatsByUser(String username) {
        List<Chat> chats = chatRepository.findByCompanyUsernameOrPersonUsername(username, username);

        return chats.stream()
                .filter(chat -> !chat.getMessageList().isEmpty()) // Фильтруем пустые чаты
                .sorted(Comparator.comparing(chat -> chat.getMessageList().stream()
                                .map(Message::getCreatingTime)
                                .max(Comparator.naturalOrder())
                                .orElse(null),
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(ChatDTO::new)
                .collect(Collectors.toList());
    }
}
