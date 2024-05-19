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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final MailService mailService;

    public Person findByUsername(String username) {
        Person person = personRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(("User with this id not found")));
            return person;
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
    public void sendActivationCode(Person person){
        String activationCode = UUID.randomUUID().toString();
        person.setActivationCode(activationCode);
        String message = String.format(
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Активация аккаунта</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 0 auto;\n" +
                        "            padding: 20px;\n" +
                        "            background-color: #fff;\n" +
                        "            border-radius: 5px;\n" +
                        "            box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                        "            text-align: center; /* Добавлено для центрирования содержимого */\n" +
                        "        }\n" +
                        "        h1 {\n" +
                        "            color: #333;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            color: #666;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        a {\n" +
                        "            color: #fff;\n" +
                        "            background-color: #007bff;\n" +
                        "            text-decoration: none;\n" +
                        "            padding: 10px 20px;\n" +
                        "            border-radius: 5px;\n" +
                        "            display: inline-block;\n" +
                        "            margin-top: 20px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <h1>Привет, %s!</h1>\n" +
                        "        <p>Добро пожаловать в Jobs. Пожалуйста, активируйте ваш аккаунт, кликнув по кнопке ниже:</p>\n" +
                        "        <a href=\"http://localhost:4200/activate/%s\">Активировать аккаунт</a>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>",
                person.getUsername(),
                activationCode
        );
        mailService.send(person.getUsername(), "Активация аккаунта", message);
    }
    public void sendPasswordCode(Person person){
        String passwordCode = UUID.randomUUID().toString();
        person.setPasswordCode(passwordCode);
        personRepository.save(person);
        String message = String.format(
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Изменение пароля</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 0 auto;\n" +
                        "            padding: 20px;\n" +
                        "            background-color: #fff;\n" +
                        "            border-radius: 5px;\n" +
                        "            box-shadow: 0 0 10px rgba(0,0,0,0.1);\n" +
                        "            text-align: center; /* Добавлено для центрирования содержимого */\n" +
                        "        }\n" +
                        "        h1 {\n" +
                        "            color: #333;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            color: #666;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        a {\n" +
                        "            color: #fff;\n" +
                        "            background-color: #007bff;\n" +
                        "            text-decoration: none;\n" +
                        "            padding: 10px 20px;\n" +
                        "            border-radius: 5px;\n" +
                        "            display: inline-block;\n" +
                        "            margin-top: 20px;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <h1>Привет, %s!</h1>\n" +
                        "        <p>Добро пожаловать в Jobs. Пожалуйста, введите новый пароль, кликнув по кнопке:</p>\n" +
                        "        <a href=\"http://localhost:4200/password/%s\">Сменить пароль</a>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>",
                person.getUsername(),
                passwordCode
        );
        mailService.send(person.getUsername(), "Изменение пароля", message);
    }

    public Person createPerson(RegistrationUserDTO registrationUserDTO) {
        if(personRepository.findByUsername(registrationUserDTO.getUsername()).isPresent()){
            throw new LocalException(HttpStatus.BAD_REQUEST,"Такой пользователь уже существует");
        }
        Person person = new Person();
        person.setPassword(passwordEncoder.encode(registrationUserDTO.getPassword()));
        person.setUsername(registrationUserDTO.getUsername());
        if(registrationUserDTO.getRole().getName().equals("ROLE_COMPANY")){
            person.setRoles(List.of(roleService.getCompanyRole()));
        }
        else {
            person.setRoles(List.of(roleService.getUserRole()));
        }
        sendActivationCode(person);
        return personRepository.save(person);
    }

    public Person activateUser(String code) {
        Person person = personRepository.findByActivationCode(code).orElseThrow(() -> new UsernameNotFoundException(("User with this activation code not found")));
        person.setActivationCode(null);
        personRepository.save(person);
        return person;
    }
    public void changePassword(String code,String newPassword) {
        System.out.println(code);
        System.out.println(newPassword);
        Person person = personRepository.findByPasswordCode(code).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this password code not found"));
        System.out.println("(((((");
        person.setPasswordCode(null);
        person.setPassword(passwordEncoder.encode(newPassword));
        personRepository.save(person);
    }
}