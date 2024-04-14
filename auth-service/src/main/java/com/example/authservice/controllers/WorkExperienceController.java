package com.example.authservice.controllers;

import com.example.authservice.DTOs.profile.work.WorkExperienceCreateDTO;
import com.example.authservice.DTOs.profile.work.WorkExperienceDTO;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.services.profile.WorkExperienceService;
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
@RequiredArgsConstructor
@RequestMapping("/api/workexperiences")
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @Operation(summary = "Get all work experiences",
            description = "Get a list of all work experiences.",
            parameters = {
            @Parameter(
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    required = true,
                    description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = WorkExperienceDTO.class))))
    })
    @GetMapping("/alll")
    public ResponseEntity<List<WorkExperienceDTO>> getAllWorkExperiences() {
        List<WorkExperienceDTO> workExperiences = workExperienceService.getAllWorkExperiences();
        return new ResponseEntity<>(workExperiences, HttpStatus.OK);
    }

    @Operation(summary = "Get all work experiences by username",
            description = "Get a list of work experiences for a specific user.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = WorkExperienceDTO.class))))
    })
    @GetMapping("/all")
    public ResponseEntity<List<WorkExperienceDTO>> getAllWorkExperiencesbyUsername(
            @RequestHeader("loadedUsername") String username) {
        List<WorkExperienceDTO> workExperiences = workExperienceService.getWorkExperienceByUsername(username);
        return new ResponseEntity<>(workExperiences, HttpStatus.OK);
    }

    @Operation(summary = "Get work experience by ID",
            description = "Get details of a specific work experience by its ID.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkExperienceDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> getWorkExperienceById(@PathVariable Long id) {
        WorkExperienceDTO workExperienceDTO = workExperienceService.getWorkExperienceDTOById(id);
        return new ResponseEntity<>(workExperienceDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create new work experience",
            description = "Create a new work experience for a user. Returns the created WorkExperienceDTO.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkExperienceDTO.class)))
    })
    @PostMapping()
    public ResponseEntity<WorkExperienceDTO> createNewWorkExperience(
            @RequestBody WorkExperienceCreateDTO createDTO,
            @RequestHeader("loadedUsername") String username) {
        WorkExperience createdWorkExperience = workExperienceService.createWorkExperience(createDTO, username);
        WorkExperienceDTO responseDTO = workExperienceService.mapWorkExperienceToDTO(createdWorkExperience);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Update work experience",
            description = "Update details of a specific work experience. Returns the updated WorkExperienceDTO.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkExperienceDTO.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<WorkExperienceDTO> updateWorkExperience(
            @PathVariable Long id,
            @RequestBody WorkExperienceDTO updateDTO) {
        WorkExperienceDTO updatedWorkExperienceDTO = workExperienceService.updateWorkExperience(id, updateDTO);
        return new ResponseEntity<>(updatedWorkExperienceDTO, HttpStatus.OK);
    }

    @Operation(summary = "Delete work experience",
            description = "Delete a specific work experience by its ID. Returns HttpStatus.NO_CONTENT on success.",
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            required = true,
                            description = "JWT Token, can be generated in auth controller /auth, starting with Bearer")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkExperience(@PathVariable Long id) {
        workExperienceService.deleteWorkExperience(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
