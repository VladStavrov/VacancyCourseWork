package com.example.authservice.services;


import com.example.authservice.DTOs.PersonDTO;
import com.example.authservice.DTOs.RegistrationUserDTO;
import com.example.authservice.exceptions.LocalException;
import com.example.authservice.models.Person;
import com.example.authservice.repositories.PersonRepository;
import com.example.authservice.requests.JwtRequest;
import com.example.authservice.responses.JwtResponse;
import com.example.authservice.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final JWTUtil jwtUtil;
    private  final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        try {
            System.out.println(authRequest);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                    authRequest.getPassword()));
        } catch(BadCredentialsException e){
            throw new LocalException(HttpStatus.UNAUTHORIZED,"Неверный логин или пароль");
        }

        UserDetails userDetails = personService.loadUserByUsername(authRequest.getUsername());

        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public PersonDTO createNewPerson(@RequestBody RegistrationUserDTO registrationUserDTO)  {
        if(!registrationUserDTO.getPassword().equals(registrationUserDTO.getConfirmPassword())){
            throw new LocalException(HttpStatus.BAD_REQUEST,"Пароли не совпадают");
        }
        if(personRepository.findByUsername(registrationUserDTO.getUsername()).isPresent()){
            throw new LocalException(HttpStatus.BAD_REQUEST,"Такой пользователь уже существует");
        }

        Person person = personService.createPerson(registrationUserDTO);
        return new PersonDTO(person.getId(), person.getUsername());
    }



}
