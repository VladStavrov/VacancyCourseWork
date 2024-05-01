package com.example.authservice.services.profile;

import com.example.authservice.DTOs.profile.work.WorkExperienceCreateDTO;
import com.example.authservice.DTOs.profile.work.WorkExperienceDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.repositories.profile.WorkExperienceRepository;
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
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final ModelMapper modelMapper;
    private final NodeService nodeService;
    private final PersonService personService;
    public List<WorkExperienceDTO> getAllWorkExperiences() {
        List<WorkExperience> workExperiences = workExperienceRepository.findAll();
        return workExperiences.stream()
                .map(this::mapWorkExperienceToDTO)
                .collect(Collectors.toList());
    }
    public WorkExperienceDTO getWorkExperienceDTOById(Long id) {
        WorkExperience workExperience = getWorkExperience(id);
        return mapWorkExperienceToDTO(workExperience);
    }
    public WorkExperience getWorkExperienceById(Long id) {
        WorkExperience workExperience = getWorkExperience(id);
        return workExperience;
    }
    public WorkExperience createWorkExperience(WorkExperienceCreateDTO createDTO, String username) {
        System.out.println(createDTO);
        WorkExperience workExperience = mapDTOToWorkExperience(createDTO);

        Person person = personService.findByUsername(username);
        Set<Node> primarySkills = createDTO.getPrimarySkills().stream()
                .map(nodeDTO -> nodeService.getNodeBySlug(nodeDTO.getSlug()))
                .collect(Collectors.toSet());
        Set<Node> secondarySkills = createDTO.getSecondarySkills().stream()
                .map(nodeDTO -> nodeService.getNodeBySlug(nodeDTO.getSlug()))
                .collect(Collectors.toSet());
        workExperience.setPrimarySkillsDB(primarySkills);
        workExperience.setSecondarySkillsDB(secondarySkills);
        workExperience.setPersonDB(person);
        WorkExperience createdWorkExperience = workExperienceRepository.save(workExperience);
        return createdWorkExperience;
    }
    public WorkExperienceDTO updateWorkExperience(Long id, WorkExperienceDTO updateDTO) {
        WorkExperience existingWorkExperience = workExperienceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkExperience not found with id: " + id));

        if (updateDTO.getCompany() != null) {
            existingWorkExperience.setCompany(updateDTO.getCompany());
        }
        if (updateDTO.getJobTitle() != null) {
            existingWorkExperience.setJobTitle(updateDTO.getJobTitle());
        }
        if (updateDTO.getDateStart() != null) {
            existingWorkExperience.setDateStart(updateDTO.getDateStart());
        }
        if (updateDTO.getDateFinish() != null) {
            existingWorkExperience.setDateFinish(updateDTO.getDateFinish());
        }
        if (updateDTO.getDescription() != null) {
            existingWorkExperience.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getPrimarySkills() != null) {
            existingWorkExperience.getPrimarySkills().clear();
            Set<Node> primarySkills = updateDTO.getPrimarySkills().stream()
                    .map(nodeDTO -> nodeService.getNodeBySlug(nodeDTO.getSlug()))
                    .collect(Collectors.toSet());
            existingWorkExperience.setPrimarySkillsDB(primarySkills);
        }
        if (updateDTO.getSecondarySkills() != null) {
            existingWorkExperience.getSecondarySkills().clear();
            Set<Node> primarySkills = updateDTO.getSecondarySkills().stream()
                    .map(nodeDTO -> nodeService.getNodeBySlug(nodeDTO.getSlug()))
                    .collect(Collectors.toSet());
            existingWorkExperience.setSecondarySkillsDB(primarySkills);
        }
        WorkExperience updatedWorkExperience = workExperienceRepository.save(existingWorkExperience);
        return mapWorkExperienceToDTO(updatedWorkExperience);
    }

    public void deleteWorkExperience(Long id) {
        WorkExperience workExperience = getWorkExperience(id);
        workExperienceRepository.delete(workExperience);
    }
    public List<WorkExperienceDTO> getWorkExperienceByUsername(String username){
        List<WorkExperience> workExperiences = workExperienceRepository.findAllByPersonUsername(username);
        return workExperiences.stream()
                .map(this::mapWorkExperienceToDTO)
                .collect(Collectors.toList());
    }
    private WorkExperience getWorkExperience(Long id) {
        return workExperienceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"WorkExperience not found with id: " + id));
    }
    public WorkExperienceDTO mapWorkExperienceToDTO(WorkExperience workExperience) {
        return modelMapper.map(workExperience, WorkExperienceDTO.class);
    }

    private WorkExperience mapDTOToWorkExperience(WorkExperienceDTO workDTO) {
        return modelMapper.map(workDTO, WorkExperience.class);
    }
    private WorkExperience mapDTOToWorkExperience(WorkExperienceCreateDTO workDTO) {
        return modelMapper.map(workDTO, WorkExperience.class);
    }


}