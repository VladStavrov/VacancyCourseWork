package com.example.authservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.authservice.DTOs.profile.ProfileDTO;
import com.example.authservice.services.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api1/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/all")
    public List<ProfileDTO> getAllProfiles() {
        System.out.println("Мы зашли");
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ProfileDTO getProfileById(@PathVariable Long id) {
        return profileService.getProfileDTOById(id);
    }

    @GetMapping("/person/{personId}")
    public ProfileDTO getProfileByPersonId(@PathVariable Long personId) {
        return profileService.getProfileByPersonId(personId);
    }

    @PostMapping("/edit")
    public ProfileDTO createProfile(@RequestBody ProfileDTO createDTO) {
        System.out.println("Редактирование");
        return profileService.createProfile(createDTO);
    }

    @PutMapping("/{id}")
    public ProfileDTO updateProfile(@PathVariable Long id, @RequestBody ProfileDTO updateDTO) {
        return profileService.updateProfile(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
    }
}
