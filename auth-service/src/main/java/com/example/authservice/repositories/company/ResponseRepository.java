package com.example.authservice.repositories.company;

import com.example.authservice.models.vacancies.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<Response,Long> {
    List<Response> findAllByPersonUsername(String username);
    List<Response> findAllByVacancyId(Long id);
    Optional<Response> findByPersonUsernameAndVacancyId(String username, Long id);
    List<Response> findAllByVacancyCompanyPersonUsername(String username);
}
