package com.example.authservice.models.chat;


import com.example.authservice.models.auth.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Column(columnDefinition = "TEXT")
    private String message;


    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creatingTime;

    @PrePersist
    protected void onCreate() {
        creatingTime = LocalDateTime.now();
    }
}