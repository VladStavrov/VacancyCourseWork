package com.example.authservice.services.auth;


import com.example.authservice.DTOs.auth.RegistrationUserDTO;
import com.example.authservice.exceptions.LocalException;
import com.example.authservice.models.auth.Person;
import com.example.authservice.repositories.auth.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        if(personRepository.findByUsername(registrationUserDTO.getUsername()).isPresent()){
            throw new LocalException(HttpStatus.BAD_REQUEST,"Такой пользователь уже существует");
        }
        Person person = new Person();
        person.setPassword(passwordEncoder.encode(registrationUserDTO.getPassword()));
        person.setUsername(registrationUserDTO.getUsername());
        person.setRoles(List.of(roleService.getUserRole(),roleService.getAdminRole()));
        return personRepository.save(person);
    }

}
