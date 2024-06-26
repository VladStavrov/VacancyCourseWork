package com.example.authservice.repositories.auth;


import com.example.authservice.models.auth.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {
    Optional<Person> findByUsername(String username);
    Optional<Person> findByActivationCode(String code);
    Optional<Person> findByPasswordCode(String code);

}
