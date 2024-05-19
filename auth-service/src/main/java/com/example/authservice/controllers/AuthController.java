package com.example.authservice.controllers;



import com.example.authservice.DTOs.auth.*;
import com.example.authservice.DTOs.profile.profile.ProfileDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.services.auth.AuthService;
import com.example.authservice.services.auth.PersonService;
import com.example.authservice.services.profile.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/identity")
public class AuthController {

    private final ProfileService profileService;
    @GetMapping("/all")
    public List<ProfileDTO> getAllProfiles() {
        System.out.println("Мы зашли");
        return profileService.getAllProfiles();
    }


    private final AuthService authService;
    private final PersonService personService;
    @PostMapping("/auth")
    @Operation(summary = "Authentication", description = "Authentication on the site")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authorization",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect email or password",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return ResponseEntity.ok(authService.authorize(authRequest));
    }
    @PostMapping("/registration")
    @Operation(summary = "Create a new user", description = "Create a new user with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "Conflict: user with such details already exists")
    })
    public ResponseEntity<?> createNewPerson(@RequestBody RegistrationUserDTO registrationUserDTO)  {
        authService.createNewPerson(registrationUserDTO);
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/refresh")
    @Operation(summary = "JWT update", description = "Getting a new access token using refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "403", description = "Refresh token is not valid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
    })
    public ResponseEntity<?> refreshtoken( @Valid @RequestBody TokenRefreshRequest request) {

        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));

    }
    @GetMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) {
            PersonDTO person = authService.activatePerson(code);
            return ResponseEntity.ok(person);
    }
    @PostMapping("/activate/send/{username}")
    public ResponseEntity<Void> sendActivationCode(@PathVariable String username) {
        authService.sendActivationCodeAuth(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/password/send/{username}")
    public ResponseEntity<Void> sendPasswordCode(@PathVariable String username) {
        authService.sendPasswordCodeAuth(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/password/change/{code}")
    public ResponseEntity<Void> changePassword(@PathVariable String code, @RequestBody ChangePasswordDTO newPassword) {
        personService.changePassword(code, newPassword.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}