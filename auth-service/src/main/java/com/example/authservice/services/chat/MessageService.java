package com.example.authservice.services.chat;

import com.example.authservice.DTOs.chat.MessageCreateDTO;
import com.example.authservice.DTOs.chat.MessageDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.models.chat.Chat;
import com.example.authservice.models.chat.Message;
import com.example.authservice.repositories.chat.ChatRepository;
import com.example.authservice.repositories.chat.MessageRepository;
import com.example.authservice.services.auth.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final PersonService personService;
    public MessageDTO createMessage(MessageCreateDTO messageCreateDTO, Long chatId ) {
        Chat chat = chatService.getChatByIdReal(chatId);

        Message message = new Message();
        message.setChat(chat);
        Person person = personService.findByUsername(messageCreateDTO.getUsername());
        message.setPerson(person);
        message.setMessage(messageCreateDTO.getMessage());
        System.out.println("chatId "+chat.getId());
        System.out.println("message "+chat.getId());
        System.out.println("chatId "+chat.getId());

        message = messageRepository.save(message);

        return new MessageDTO(message);
    }
    public List<MessageDTO> getAllMessagesByChatId(Long chatId) {
        List<Message> messages = messageRepository.findByChatIdOrderByCreatingTimeAsc(chatId);
        return messages.stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());
    }
}
