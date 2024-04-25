package com.example.authservice.controllers;

import com.example.authservice.DTOs.profile.profile.ProfileCreateDTO;
import com.example.authservice.DTOs.profile.profile.ProfileDTO;
import com.example.authservice.services.profile.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "Get all profiles",
            description = "Get a list of all profiles.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProfileDTO.class))))
    })
    @GetMapping("/all")
    public ResponseEntity<List<ProfileDTO>> getAllProfiles() {
        List<ProfileDTO> profiles = profileService.getAllProfiles();
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @Operation(summary = "Get profile by username",
            description = "Get details of a specific user's profile.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileDTO.class)))
    })
    @GetMapping("/{username}")
    public ResponseEntity<ProfileDTO> getProfileByUsername(@PathVariable String username) {
        ProfileDTO profileDTO = profileService.getProfileDTOByUsername(username);
        return new ResponseEntity<>(profileDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create new profile",
            description = "Create a new profile for a user. Returns the created ProfileDTO.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ProfileDTO> createProfile(
            @RequestHeader("loadedUsername") String username,
            @RequestBody ProfileCreateDTO createDTO) {
        ProfileDTO createdProfile = profileService.createProfile(createDTO, username);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @Operation(summary = "Update profile",
            description = "Update details of a specific user's profile. Returns the updated ProfileDTO.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileDTO.class)))
    })
    @PutMapping
    public ResponseEntity<ProfileDTO> updateProfile(
            @RequestHeader("loadedUsername") String username,
            @RequestBody ProfileDTO updateDTO) {
        ProfileDTO updatedProfile = profileService.updateProfile(username, updateDTO);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @Operation(summary = "Delete profile",
            description = "Delete a specific user's profile. Returns HttpStatus.NO_CONTENT on success.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content")
    })
    @DeleteMapping()
    public ResponseEntity<Void> deleteProfile(@RequestHeader("loadedUsername") String username) {
        profileService.deleteProfile(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}