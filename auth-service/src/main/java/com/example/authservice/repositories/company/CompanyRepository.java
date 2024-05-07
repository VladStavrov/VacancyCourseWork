package com.example.authservice.repositories.company;

import com.example.authservice.models.profile.Node;
import com.example.authservice.models.vacancies.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@RepositoryRestResource(exported = false)
public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findByPersonUsername(String username);
    Optional<Company> findByCompanyName(String companyName);
}
