package com.example.authservice.repositories.auth;


import com.example.authservice.models.auth.Person;
import com.example.authservice.models.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByPerson(Person person);
    @Modifying
    int deleteByPerson(Person person);
}
