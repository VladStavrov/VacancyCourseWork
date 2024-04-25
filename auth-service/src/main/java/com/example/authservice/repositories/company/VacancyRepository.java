package com.example.authservice.repositories.company;

import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.models.vacancies.Vacancies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface VacancyRepository extends JpaRepository<Vacancies,Long> {
    List<Vacancies> findALlByCompany_Person_Username(String username);
}
