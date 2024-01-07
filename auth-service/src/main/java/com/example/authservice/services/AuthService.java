package com.example.authservice.services;


import com.example.authservice.DTOs.PersonDTO;
import com.example.authservice.DTOs.RegistrationUserDTO;
import com.example.authservice.exceptions.LocalException;
import com.example.authservice.exceptions.TokenRefreshException;
import com.example.authservice.models.Person;

import com.example.authservice.DTOs.JwtRequest;
import com.example.authservice.DTOs.JwtResponse;
import com.example.authservice.models.RefreshToken;
import com.example.authservice.utils.JWTUtil;
import jakarta.servlet.ServletException;
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

    private final JWTUtil jwtUtil;
    private  final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    public JwtResponse authorize(@RequestBody JwtRequest authRequest){
        try {
            System.out.println(authRequest);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                    authRequest.getPassword()));
        } catch(BadCredentialsException e){
            throw new LocalException(HttpStatus.UNAUTHORIZED,"Неверный логин или пароль");
        }
        Person person = personService.findByUsername(authRequest.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(person);
        String token = null;
        try {
            token = jwtUtil.generateToken(person);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return new JwtResponse(token,refreshToken.getToken());
    }

    public PersonDTO createNewPerson(@RequestBody RegistrationUserDTO registrationUserDTO)  {
        if(!registrationUserDTO.getPassword().equals(registrationUserDTO.getConfirmPassword())){
            throw new LocalException(HttpStatus.BAD_REQUEST,"Пароли не совпадают");
        }
        Person person = personService.createPerson(registrationUserDTO);
        return new PersonDTO(person.getId(), person.getUsername());
    }
    public JwtResponse refreshToken(String requestRefreshToken){
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getPerson)
                .map(user -> {
                    String token = null;
                    try {
                        token = jwtUtil.generateToken(user);
                    } catch (ServletException e) {
                        throw new RuntimeException(e);
                    }
                    return new JwtResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }



}
