package com.example.authservice.DTOs.company.company;

import com.example.authservice.DTOs.company.vacancy.VacancyDTO;
import com.example.authservice.models.vacancies.Company;
import com.example.authservice.models.vacancies.Location;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
    public class CompanyDTO {
        private String companyName;
        private Location location;
        private String description;
        private String email;
        private String phoneNumber;
        private Long personId;
        private List<VacancyDTO> vacancyDTOS;

        public CompanyDTO(Company company) {
            this.companyName = company.getCompanyName();
            this.location = company.getLocation();
            this.description = company.getDescription();
            this.email = company.getEmail();
            this.phoneNumber = company.getPhoneNumber();
            if (company.getPerson() != null) {
                this.personId = company.getPerson().getId();
            }
            this.vacancyDTOS = company.getVacancies().stream().map(VacancyDTO::new).collect(Collectors.toList());
        }
    }

