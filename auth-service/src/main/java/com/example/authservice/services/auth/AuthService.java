package com.example.authservice.services.auth;


import com.example.authservice.DTOs.auth.PersonDTO;
import com.example.authservice.DTOs.auth.RegistrationUserDTO;
import com.example.authservice.exceptions.LocalException;
import com.example.authservice.exceptions.TokenRefreshException;
import com.example.authservice.models.auth.Person;


import com.example.authservice.DTOs.auth.JwtRequest;
import com.example.authservice.DTOs.auth.JwtResponse;
import com.example.authservice.models.auth.RefreshToken;
import com.example.authservice.models.auth.Role;
import com.example.authservice.utils.JWTUtil;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final PersonService personService;

    private final JWTUtil jwtUtil;
    private  final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    public JwtResponse authorize(@RequestBody JwtRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                    authRequest.getPassword()));
        } catch(BadCredentialsException e){
            throw new LocalException(HttpStatus.UNAUTHORIZED,"Неверный логин или пароль");
        }
        Person person = personService.findByUsername(authRequest.getUsername());
        if(person.getActivationCode() != null){
            throw new LocalException(HttpStatus.FORBIDDEN,"Пользователь не активирован");
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(person);
        String token = null;
        try {
            token = jwtUtil.generateToken((com.example.authservice.models.auth.Person)person);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        List <String>  roles=person.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        return new JwtResponse(token,refreshToken.getToken(),roles);
    }

    public PersonDTO createNewPerson(@RequestBody RegistrationUserDTO registrationUserDTO)  {
        if(!registrationUserDTO.getPassword().equals(registrationUserDTO.getConfirmPassword())){
            throw new LocalException(HttpStatus.BAD_REQUEST,"Пароли не совпадают");
        }
        Person person = personService.createPerson(registrationUserDTO);
        return new PersonDTO(person.getId(), person.getUsername());
    }
    public void sendActivationCodeAuth(String username){
        Person person = personService.findByUsername(username);
        personService.sendActivationCode(person);
    }

    public void sendPasswordCodeAuth(String username){
        Person person = personService.findByUsername(username);
        personService.sendPasswordCode(person);
    }
    public JwtResponse refreshToken(String requestRefreshToken){
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getPerson)
                .map(user -> {
                    String token = null;
                    try {
                        token = jwtUtil.generateToken((com.example.authservice.models.auth.Person) user);
                    } catch (ServletException e) {
                        throw new RuntimeException(e);
                    }
                    List <String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
                    return new JwtResponse(token, requestRefreshToken,roles);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    public PersonDTO activatePerson(String code){
        Person person = personService.activateUser(code);
        return new PersonDTO(person.getId(), person.getUsername());
    }

}
