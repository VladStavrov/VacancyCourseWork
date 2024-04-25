package com.example.authservice.controllers;

import com.example.authservice.DTOs.company.company.CompanyCreateDTO;
import com.example.authservice.DTOs.company.company.CompanyDTO;
import com.example.authservice.services.company.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{username}")
    public ResponseEntity<CompanyDTO> getCompanyByUsername(@PathVariable String username) {
        CompanyDTO company = companyService.getCompanyDTOByUsername(username);
        return ResponseEntity.ok(company);
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@Valid @RequestBody CompanyCreateDTO companyCreateDTO,  @RequestHeader("loadedUsername") String username) {
        CompanyDTO createdCompany = companyService.createCompany(companyCreateDTO, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @PutMapping()
    public ResponseEntity<CompanyDTO> updateCompany( @RequestHeader("loadedUsername") String username, @Valid @RequestBody CompanyCreateDTO companyCreateDTO) {
        CompanyDTO updatedCompany = companyService.updateCompany(username, companyCreateDTO);
        if (updatedCompany != null) {
            return ResponseEntity.ok(updatedCompany);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteCompany(@RequestHeader("loadedUsername") String username) {
        companyService.deleteCompany(username);
        return ResponseEntity.noContent().build();
    }
}