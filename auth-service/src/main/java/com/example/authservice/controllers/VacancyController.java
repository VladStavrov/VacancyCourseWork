package com.example.authservice.controllers;

import com.example.authservice.DTOs.company.vacancy.VacancyCreateDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyFilterDTO;
import com.example.authservice.DTOs.profile.work.WorkExperienceDTO;
import com.example.authservice.services.company.VacancyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    @Autowired
    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping
    public ResponseEntity<List<VacancyDTO>> getAllVacancies() {
        List<VacancyDTO> vacancies = vacancyService.getAllVacanciesDTO();
        return ResponseEntity.ok(vacancies);
    }
    @GetMapping("/all/{username}")
    public ResponseEntity<List<VacancyDTO>> getAllVacanciesByUsername(
            @PathVariable String username) {
        List<VacancyDTO> vacancyDTOS = vacancyService.getVacancyByUsername(username);
        return new ResponseEntity<>(vacancyDTOS, HttpStatus.OK);
    }
    @PostMapping("/vacancies")
    public ResponseEntity<List<VacancyDTO>> getFilteredAndSortedVacancies(
            @RequestBody VacancyFilterDTO filterDTO,
            @RequestParam(value = "username", required = false) String username
    ) {
        List<VacancyDTO> filteredVacancies;

            filteredVacancies = vacancyService.getFilteredAndSortedVacancies(filterDTO, username);

        return new ResponseEntity<>(filteredVacancies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacancyDTO> getVacancyById(@PathVariable Long id) {
        VacancyDTO vacancy = vacancyService.getVacancyDTOById(id);
        return ResponseEntity.ok(vacancy);
    }

    @PostMapping("/{id}")
    public ResponseEntity<VacancyDTO> createVacancy(@PathVariable Long id,@Valid @RequestBody VacancyCreateDTO vacancyCreateDTO) {
        VacancyDTO createdVacancy = vacancyService.createVacancy(vacancyCreateDTO, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVacancy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyDTO> updateVacancy(@PathVariable Long id, @Valid @RequestBody VacancyDTO vacancyCreateDTO) {
        VacancyDTO updatedVacancy = vacancyService.updateVacancy(id,vacancyCreateDTO);
        if (updatedVacancy != null) {
            return ResponseEntity.ok(updatedVacancy);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {

        vacancyService.deleteVacancy(id);
        return ResponseEntity.noContent().build();
    }
}