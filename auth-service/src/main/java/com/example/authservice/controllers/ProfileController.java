package com.example.authservice.controllers;

import com.example.authservice.DTOs.profile.ProfileCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.authservice.DTOs.profile.ProfileDTO;
import com.example.authservice.services.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;


    @GetMapping
    public ResponseEntity<List<ProfileDTO>> getAllProfiles() {
        List<ProfileDTO> profiles = profileService.getAllProfiles();
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDTO> getProfileByUsername(@PathVariable String username) {
        ProfileDTO profileDTO = profileService.getProfileDTOByUsername(username);
        return new ResponseEntity<>(profileDTO, HttpStatus.OK);
    }

    @PostMapping("/{username}")
    public ResponseEntity<ProfileDTO> createProfile(@PathVariable String username, @RequestBody ProfileCreateDTO createDTO) {
        ProfileDTO createdProfile = profileService.createProfile(createDTO, username);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    public ResponseEntity<ProfileDTO> updateProfile(@PathVariable String username, @RequestBody ProfileDTO updateDTO) {
        ProfileDTO updatedProfile = profileService.updateProfile(username, updateDTO);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String username) {
        profileService.deleteProfile(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
