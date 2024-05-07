package com.example.authservice.repositories.chat;

import com.example.authservice.models.auth.Person;
import com.example.authservice.models.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository <Chat,Long> {
    List<Chat> findByCompanyUsernameOrPersonUsername(String person1,String person2);
    Chat findByIdOrderByMessageListCreatingTimeAsc(Long chatId);

    Optional<Chat> findByCompanyUsernameAndPersonUsername(String person1, String person2);

}
