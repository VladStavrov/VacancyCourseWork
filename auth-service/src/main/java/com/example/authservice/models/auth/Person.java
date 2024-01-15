package com.example.authservice.models.auth;

import com.example.authservice.models.profile.Profile;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Collection;

@Entity
@Data
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;


    @OneToOne(mappedBy = "person")
    private RefreshToken refreshToken;


    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection <Role> roles;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

}