package com.example.authservice.DTOs.company.response;

import com.example.authservice.DTOs.company.vacancy.VacancyDTO;
import com.example.authservice.models.vacancies.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private Long id;
    private String username;
    private VacancyDTO vacancy;
    private String status;
    public ResponseDTO(Response response){
        this.id = response.getId();
        this.username = response.getPerson().getUsername();
        this.status = response.getStatus();
        this.vacancy = new VacancyDTO(response.getVacancy());
    }
}