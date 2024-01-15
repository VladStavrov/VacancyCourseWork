package com.example.authservice.services.profile;

import com.example.authservice.DTOs.profile.ProfileDTO;
import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.Profile;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.repositories.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final NodeService nodeService;
    private final WorkExperienceService workExperienceService;
    private final ModelMapper modelMapper;

    public List<ProfileDTO> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();
        return profiles.stream()
                .map(this::mapProfileToDTO)
                .collect(Collectors.toList());
    }

    public ProfileDTO getProfileDTOById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found with id: " + id));
        ;
        return mapProfileToDTO(profile);
    }
    public Profile getProfileById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found with id: " + id));
        return profile;
    }
    public ProfileDTO getProfileByPersonId(Long personId) {
        Profile profile = profileRepository.findByPersonId(personId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found with personId: " + personId));
        return mapProfileToDTO(profile);
    }

    public ProfileDTO createProfile(ProfileDTO createDTO) {
        Profile profile = mapDTOToProfile(createDTO);
        Profile createdProfile = profileRepository.save(profile);
        return mapProfileToDTO(createdProfile);
    }

    public ProfileDTO updateProfile(Long id, ProfileDTO updateDTO) {
        Profile existingProfile = getProfileById(id);

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
            existingProfile.setKnowledge(
                    updateDTO.getKnowledge().stream()
                            .map(skillDTO -> {
                                Node skill;
                                try {
                                    skill = nodeService.getNodeBySlug(skillDTO.getSlug());
                                } catch (ResponseStatusException e) {
                                    skill= nodeService.createNode(skillDTO);

                                }
                                return skill;
                            })
                            .collect(Collectors.toSet())
            );
        }
        if (updateDTO.getWorkExperiences() != null) {

            existingProfile.setWorkExperiences(
                    updateDTO.getWorkExperiences().stream()
                            .map(workExperienceDTO -> {
                                WorkExperience workExperience;
                                try{
                                    workExperience= workExperienceService.getWorkExperienceById(workExperienceDTO.getId());
                                }
                                catch (ResponseStatusException e){
                                    workExperience=workExperienceService.createWorkExperience(workExperienceDTO);

                                }
                                workExperience.setProfile(existingProfile);
                                return workExperience;
                            })
                            .collect(Collectors.toList())
            );
        }

        Profile updatedProfile = profileRepository.save(existingProfile);
        return mapProfileToDTO(updatedProfile);
    }



    public void deleteProfile(Long id) {
        Profile profile = getProfileById(id);
        profileRepository.delete(profile);
    }

    private ProfileDTO mapProfileToDTO(Profile profile) {
        return modelMapper.map(profile, ProfileDTO.class);
    }

    private Profile mapDTOToProfile(ProfileDTO profileDTO) {
        return modelMapper.map(profileDTO, Profile.class);
    }

}
