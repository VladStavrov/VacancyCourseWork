package com.example.authservice.services;


import com.example.authservice.DTOs.RegistrationUserDTO;
import com.example.authservice.models.Person;
import com.example.authservice.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public Person findByUsername(String username) {
        return personRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(("User with this id not found")));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = findByUsername(username);
        return new User(
                person.getUsername(),
                person.getPassword(),
                person.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public Person createPerson(RegistrationUserDTO registrationUserDTO) {
        Person person = new Person();
        person.setPassword(passwordEncoder.encode(registrationUserDTO.getPassword()));
        person.setUsername(registrationUserDTO.getUsername());
        person.setRoles(List.of(roleService.getUserRole()));
        return personRepository.save(person);
    }

}
