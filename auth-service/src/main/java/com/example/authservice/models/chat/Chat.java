package com.example.authservice.models.chat;

import com.example.authservice.models.auth.Person;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Person company;

    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;


    @OneToMany(mappedBy = "chat",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();
}
