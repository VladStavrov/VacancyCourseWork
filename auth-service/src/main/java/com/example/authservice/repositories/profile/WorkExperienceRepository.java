package com.example.authservice.repositories.profile;

import com.example.authservice.models.profile.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience,Long> {
    List<WorkExperience> findAllByPersonUsername(String username);

}
