package com.example.authservice.services.profile;

import com.example.authservice.DTOs.profile.NodeDTO;
import com.example.authservice.DTOs.profile.WorkExperienceDTO;
import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.repositories.profile.WorkExperienceRepository;
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
public class WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final ModelMapper modelMapper;
    private final NodeService nodeService;

    public List<WorkExperienceDTO> getAllWorkExperiences() {
        List<WorkExperience> workExperiences = workExperienceRepository.findAll();
        return workExperiences.stream()
                .map(this::mapWorkExperienceToDTO)
                .collect(Collectors.toList());
    }

    public WorkExperienceDTO getWorkExperienceDTOById(Long id) {
        WorkExperience workExperience = getWorkExperience(id);
        System.out.println("--------"+workExperience);
        return mapWorkExperienceToDTO(workExperience);
    }
    public WorkExperience getWorkExperienceById(Long id) {
        WorkExperience workExperience = getWorkExperience(id);
        return workExperience;
    }


    public WorkExperience createWorkExperience(WorkExperienceDTO createDTO) {
        WorkExperience workExperience = mapDTOToWorkExperience(createDTO);

        Set<Node> primarySkills = updateSkills(createDTO.getPrimarySkills());
        Set<Node> secondarySkills = updateSkills(createDTO.getSecondarySkills());



        workExperience.setPrimarySkills(primarySkills);
        workExperience.setSecondarySkills(secondarySkills);

        primarySkills.forEach(skill->{
            skill.getPrimarySkillExperiences().add(workExperience);
        });
        secondarySkills.forEach(skill->{
            skill.getSecondarySkillExperiences().add(workExperience);
        });

        WorkExperience createdWorkExperience = workExperienceRepository.save(workExperience);
        return createdWorkExperience;
    }
    private Set<Node>  updateSkills( Set<NodeDTO> newSkillDTOs) {
        Set<Node> nodeList = new HashSet<>();
        for (NodeDTO skillDTO : newSkillDTOs) {
            Node skill;
            try {
                skill = nodeService.getNodeBySlug(skillDTO.getSlug());
            } catch (ResponseStatusException e) {
                skill = nodeService.createNode(skillDTO);
           }
            nodeList.add(skill);
        }
        return nodeList;
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
            Set<Node> primarySkills=updateSkills(updateDTO.getPrimarySkills());
            existingWorkExperience.setPrimarySkills(primarySkills);
        }
        if (updateDTO.getSecondarySkills() != null) {
            existingWorkExperience.getSecondarySkills().clear();
            Set<Node> primarySkills=updateSkills(updateDTO.getSecondarySkills());
            existingWorkExperience.setSecondarySkills(primarySkills);
        }
        WorkExperience updatedWorkExperience = workExperienceRepository.save(existingWorkExperience);
        return mapWorkExperienceToDTO(updatedWorkExperience);
    }
    public void deleteWorkExperience(Long id) {
        WorkExperience workExperience = getWorkExperience(id);
        workExperienceRepository.delete(workExperience);
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


}