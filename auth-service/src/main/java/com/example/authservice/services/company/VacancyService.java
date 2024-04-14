package com.example.authservice.services.company;

import com.example.authservice.DTOs.company.vacancy.VacancyCreateDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyDTO;
import com.example.authservice.models.profile.Node;
import com.example.authservice.models.vacancies.Company;
import com.example.authservice.models.vacancies.Vacancies;
import com.example.authservice.repositories.company.VacancyRepository;
import com.example.authservice.services.auth.PersonService;
import com.example.authservice.services.profile.NodeService;
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
public class VacancyService {


    private final VacancyRepository vacancyRepository;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;
    private final NodeService nodeService;
    public List<VacancyDTO> getAllVacancies() {
        List<Vacancies> vacancies = vacancyRepository.findAll();
        return vacancies.stream().map(VacancyDTO::new).collect(Collectors.toList());
    }
    public Vacancies getVacancyById(Long id) {
        return vacancyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacancies not found with id: " + id));
    }

    public VacancyDTO getVacancyDTOById(Long id) {
        Vacancies vacancy = getVacancyById(id);
        return new VacancyDTO(vacancy);
    }

    public VacancyDTO createVacancy(VacancyCreateDTO vacancyCreateDTO) {
        Company company = companyService.getCompanyById(vacancyCreateDTO.getCompanyId());
        Vacancies vacancy = mapDTOToVacancy(vacancyCreateDTO);
        Set<Node> skills = vacancyCreateDTO.getSkills().stream()
                .map(nodeDTO -> nodeService.getNodeBySlug(nodeDTO.getSlug()))
                .collect(Collectors.toSet());
        vacancy.setSkillsDB(skills);
        vacancy.setCompany(company);
        Vacancies savedVacancy = vacancyRepository.save(vacancy);
        return new VacancyDTO(savedVacancy);
    }

    public VacancyDTO updateVacancy(Long id, VacancyDTO vacancyUpdateDTO) {
        Vacancies existingVacancy = vacancyRepository.findById(id).orElse(null);
        if (existingVacancy != null) {
            if (vacancyUpdateDTO.getTitle() != null) {
                existingVacancy.setTitle(vacancyUpdateDTO.getTitle());
            }
            if (vacancyUpdateDTO.getSalary() != null) {
                existingVacancy.setSalary(vacancyUpdateDTO.getSalary());
            }
            if (vacancyUpdateDTO.getExperienceLevel() != null) {
                existingVacancy.setExperienceLevel(vacancyUpdateDTO.getExperienceLevel());
            }
            if (vacancyUpdateDTO.getDescription() != null) {
                existingVacancy.setDescription(vacancyUpdateDTO.getDescription());
            }
            if (vacancyUpdateDTO.getSkills() != null) {
                existingVacancy.getSkills().clear();
                Set<Node> skills = vacancyUpdateDTO.getSkills().stream()
                        .map(nodeDTO -> nodeService.getNodeBySlug(nodeDTO.getSlug()))
                        .collect(Collectors.toSet());
                existingVacancy.setSkillsDB(skills);
            }
            Vacancies updatedVacancy = vacancyRepository.save(existingVacancy);
            return mapVacancyToDTO(updatedVacancy);
        }

        return null;
    }

    public void deleteVacancy(Long id) {
        vacancyRepository.deleteById(id);
    }
    private VacancyDTO mapVacancyToDTO(Vacancies vacancy) {
        return modelMapper.map(vacancy, VacancyDTO.class);
    }

    private Vacancies mapDTOToVacancy(VacancyDTO vacancyDTO) {
        return modelMapper.map(vacancyDTO, Vacancies.class);
    }

    private Vacancies mapDTOToVacancy(VacancyCreateDTO vacancyCreateDTO) {
        return modelMapper.map(vacancyCreateDTO, Vacancies.class);
    }
}