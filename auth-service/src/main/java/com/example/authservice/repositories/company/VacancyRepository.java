package com.example.authservice.repositories.company;

import com.example.authservice.models.vacancies.Vacancies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface VacancyRepository extends JpaRepository<Vacancies,Long> {
}
