package com.example.authservice.services.profile;

import com.example.authservice.DTOs.profile.NodeDTO;
import com.example.authservice.DTOs.profile.ProfileCreateDTO;
import com.example.authservice.DTOs.profile.ProfileDTO;
import com.example.authservice.DTOs.profile.WorkExperienceDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.Profile;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.repositories.profile.ProfileRepository;
import com.example.authservice.services.auth.PersonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final NodeService nodeService;
    private final WorkExperienceService workExperienceService;
    private final ModelMapper modelMapper;
    private final PersonService personService;

    public List<ProfileDTO> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();
        return profiles.stream()
                .map(this::mapProfileToDTO)
                .collect(Collectors.toList());
    }

    public ProfileDTO getProfileDTOByUsername(String username) {
        Profile profile = profileRepository.findByPersonUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found with username: " + username));
        ;
        return mapProfileToDTO(profile);
    }
    public Profile getProfileByUsername(String username) {
        Profile profile = profileRepository.findByPersonUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found with username: " + username));
        ;
        return profile;
    }


    public ProfileDTO createProfile(ProfileCreateDTO createDTO, String username) {
        Profile profile = mapDTOToProfile(createDTO);
        List<WorkExperience> workExperienceList = new ArrayList<>();
        createDTO.getWorkExperiences().forEach(workExperienceDTO -> {
            workExperienceList.add( workExperienceService.createWorkExperience(workExperienceDTO));

        });
        Set<Node> knowlegeList = nodeService.updateSkills(createDTO.getKnowledge());
        Person person = personService.findByUsername(username);

        profile.setKnowledge(knowlegeList);
        knowlegeList.forEach(knowlege->{
            knowlege.getProfiles().add(profile);
        });

        profile.setPerson(person);
        person.setProfile(profile);

        profile.setWorkExperiences(workExperienceList);
        workExperienceList.forEach(workExperience -> {
            workExperience.setProfile(profile);
        });
        Profile createdProfile = profileRepository.save(profile);
        return mapProfileToDTO(createdProfile);
    }

    public ProfileDTO updateProfile(String username, ProfileDTO updateDTO) {
        Profile existingProfile = getProfileByUsername(username);

        if (updateDTO.getFirstName() != null) {
            existingProfile.setFirstName(updateDTO.getFirstName());
        }

        if (updateDTO.getLastName() != null) {
            existingProfile.setLastName(updateDTO.getLastName());
        }

        if (updateDTO.getJobTitle() != null) {
            existingProfile.setJobTitle(updateDTO.getJobTitle());
        }

        if (updateDTO.getAbout() != null) {
            existingProfile.setAbout(updateDTO.getAbout());
        }

        if (updateDTO.getKnowledge() != null) {
            Set<Node> newKnowledge = nodeService.updateSkills(updateDTO.getKnowledge());
            existingProfile.setKnowledge(newKnowledge);
        }
        if (updateDTO.getWorkExperiences() != null) {
            existingProfile.setWorkExperiences(workExperienceService.updateWorkExperience(updateDTO.getWorkExperiences()));
        }

        Profile updatedProfile = profileRepository.save(existingProfile);
        return mapProfileToDTO(updatedProfile);
    }



    public void deleteProfile(String username) {
        Profile profile = getProfileByUsername(username);
        profileRepository.delete(profile);
    }

    private ProfileDTO mapProfileToDTO(Profile profile) {
        return modelMapper.map(profile, ProfileDTO.class);
    }

    private Profile mapDTOToProfile(ProfileDTO profileDTO) {
        return modelMapper.map(profileDTO, Profile.class);
    }
    private Profile mapDTOToProfile(ProfileCreateDTO profileDTO) {
        return modelMapper.map(profileDTO, Profile.class);
    }

}
