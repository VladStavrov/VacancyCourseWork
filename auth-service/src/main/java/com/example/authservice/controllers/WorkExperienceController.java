package com.example.authservice.controllers;

import com.example.authservice.DTOs.profile.WorkExperienceDTO;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.services.profile.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api1/workexperiences")
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @GetMapping
    public ResponseEntity<List<WorkExperienceDTO>> getAllWorkExperiences() {
        List<WorkExperienceDTO> workExperiences = workExperienceService.getAllWorkExperiences();
        return new ResponseEntity<>(workExperiences, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> getWorkExperienceById(@PathVariable Long id) {
        WorkExperienceDTO workExperienceDTO = workExperienceService.getWorkExperienceDTOById(id);
        return new ResponseEntity<>(workExperienceDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WorkExperienceDTO> createWorkExperience(@RequestBody WorkExperienceDTO createDTO) {
        WorkExperience createdWorkExperience = workExperienceService.createWorkExperience(createDTO);
        WorkExperienceDTO responseDTO = workExperienceService.mapWorkExperienceToDTO(createdWorkExperience);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> updateWorkExperience(
            @PathVariable Long id,
            @RequestBody WorkExperienceDTO updateDTO) {
        WorkExperienceDTO updatedWorkExperienceDTO = workExperienceService.updateWorkExperience(id, updateDTO);
        return new ResponseEntity<>(updatedWorkExperienceDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkExperience(@PathVariable Long id) {
        workExperienceService.deleteWorkExperience(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}