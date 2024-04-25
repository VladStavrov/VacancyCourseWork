package com.example.authservice.services.profile;

import com.example.authservice.DTOs.profile.profile.ProfileCreateDTO;
import com.example.authservice.DTOs.profile.profile.ProfileDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.Profile;
import com.example.authservice.repositories.profile.ProfileRepository;
import com.example.authservice.services.auth.PersonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final NodeService nodeService;
    private final ModelMapper modelMapper;
    private final PersonService personService;

    public List<ProfileDTO> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();
        return profiles.stream()
                .map(this::mapProfileToDTO)
                .collect(Collectors.toList());
    }
    public List<ProfileDTO> getAllProfilesByUsername(String username) {
        List<Profile> profiles = profileRepository.findAllByPersonUsername(username);
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
        return profile;
    }


    public ProfileDTO createProfile(ProfileCreateDTO createDTO, String username) {
        Profile profile = mapDTOToProfile(createDTO);


        Set<Node> skills = createDTO.getSkills().stream()
                .map(nodeDTO -> nodeService.getNodeBySlug(nodeDTO.getSlug()))
                .collect(Collectors.toSet());

        Person person = personService.findByUsername(username);

        profile.setPersonDB(person);

        profile.setSkillsSB(skills);

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

        if (updateDTO.getSkills() != null) {
            Set<Node> skills = updateDTO.getSkills().stream()
                    .map(nodeDTO -> nodeService.getNodeBySlug(nodeDTO.getSlug()))
                    .collect(Collectors.toSet());
            existingProfile.setSkillsSB(skills);
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
