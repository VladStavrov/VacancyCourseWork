package com.example.authservice.repositories.profile;

import com.example.authservice.models.profile.Profile;
import com.example.authservice.models.profile.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Optional<Profile> findByPersonUsername(String username);
    List<Profile> findAllByPersonUsername(String username);
}
