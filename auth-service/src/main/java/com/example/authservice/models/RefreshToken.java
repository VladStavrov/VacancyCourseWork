package com.example.authservice.models;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Entity(name = "refreshtoken")
@Data
public class RefreshToken {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;


    //getters and setters

}