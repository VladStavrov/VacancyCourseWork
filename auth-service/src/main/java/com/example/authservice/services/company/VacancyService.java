package com.example.authservice.services.company;

import com.example.authservice.DTOs.company.vacancy.VacancyCreateDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyFilterDTO;
import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.DTOs.profile.work.WorkExperienceDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.Profile;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.models.vacancies.Company;
import com.example.authservice.models.vacancies.SortType;
import com.example.authservice.models.vacancies.Vacancies;
import com.example.authservice.repositories.company.VacancyRepository;
import com.example.authservice.services.auth.PersonService;
import com.example.authservice.services.profile.NodeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyService {


    private final VacancyRepository vacancyRepository;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;
    private final NodeService nodeService;
    private final PersonService personService;
    public List<VacancyDTO> getAllVacanciesDTO() {
        return getAllVacancies().stream().map(VacancyDTO::new).collect(Collectors.toList());
    }
    public List<VacancyDTO> getVacancyByUsername(String username){
        List<Vacancies> vacancies = vacancyRepository.findALlByCompany_Person_Username(username);
        return vacancies.stream()
                .map(VacancyDTO::new)
                .collect(Collectors.toList());
    }
    public List<Vacancies> getAllVacancies() {
        return vacancyRepository.findAll();
    }
    public Vacancies getVacancyById(Long id) {
        return vacancyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vacancies not found with id: " + id));
    }

    public VacancyDTO getVacancyDTOById(Long id) {
        Vacancies vacancy = getVacancyById(id);
        return new VacancyDTO(vacancy);
    }

    public VacancyDTO createVacancy(VacancyCreateDTO vacancyCreateDTO,Long  companyId) {
        Company company = companyService.getCompanyById(companyId);
        Vacancies vacancy = mapDTOToVacancy(vacancyCreateDTO);
        vacancy.setId(null);
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

    @Transactional
    public void deleteVacancy(Long id) {
        Vacancies vacancies = getVacancyById(id);
        Company company = companyService.getCompanyById(vacancies.getCompany().getId());
        company.getVacancies().remove(vacancies);
        vacancyRepository.delete(vacancies);
    }

    public List<VacancyDTO> getFilteredAndSortedVacancies(VacancyFilterDTO filterDTO, String username) {
        List<Vacancies> allVacancies = getAllVacancies();
        List<Vacancies> sortedVacancies;

        if (filterDTOIsEmpty(filterDTO)) {
            sortedVacancies = allVacancies;
        } else {

            sortedVacancies = sortVacancies(allVacancies, filterDTO.getSortType());
        }

        return sortedVacancies.stream()
                .filter(vacancy -> filterByCriteria(vacancy, filterDTO))
                .map(VacancyDTO::new)
                .collect(Collectors.toList());
    }

    private boolean filterDTOIsEmpty(VacancyFilterDTO filterDTO) {
        return filterDTO == null ||
                (filterDTO.getSortType() == null &&
                        filterDTO.getTitle() == null &&
                        filterDTO.getMinSalary() == 0 &&
                        filterDTO.getExperienceLevel() == null &&
                        filterDTO.getCountry() == null &&
                        filterDTO.getCity() == null &&
                        (filterDTO.getSkills() == null || filterDTO.getSkills().isEmpty()));
    }

    private List<Vacancies> sortVacancies(List<Vacancies> vacancies, SortType sortType) {
        if(sortType == null){
            return vacancies;
        }
        switch (sortType) {
            case PRICE_ASCENDING:
                return vacancies.stream()
                        .sorted(Comparator.comparingDouble(vacancy -> vacancy.getSalary().getMinSalary()))
                        .collect(Collectors.toList());
            case PRICE_DESCENDING:
                List<Vacancies> filteredList = vacancies.stream()
                        .sorted(Comparator.comparingDouble(vacancy -> vacancy.getSalary().getMinSalary()))
                        .collect(Collectors.toList());
                Collections.reverse(filteredList);
                return filteredList;
            case CREATING_DATE_ASCENDING:
                return vacancies.stream()
                        .sorted(Comparator.comparing(Vacancies::getCreatingTime))
                        .collect(Collectors.toList());
            case CREATING_DATE_DESCENDING:
                return vacancies.stream()
                        .sorted(Comparator.comparing(Vacancies::getCreatingTime).reversed())
                        .collect(Collectors.toList());
            default:
                return vacancies;
        }
    }

    private boolean filterByCriteria(Vacancies vacancy, VacancyFilterDTO filterDTO) {
        return (filterDTO.getTitle() == null || vacancy.getTitle().contains(filterDTO.getTitle())) &&
                (filterDTO.getMinSalary() == 0 || vacancy.getSalary() != null && vacancy.getSalary().getMinSalary() >= filterDTO.getMinSalary()) &&
                (filterDTO.getExperienceLevel() == null || vacancy.getExperienceLevel() == filterDTO.getExperienceLevel()) &&
                (filterDTO.getCountry() == null || vacancy.getCompany().getLocation().getCountry().equals(filterDTO.getCountry())) &&
                (filterDTO.getCity() == null || vacancy.getCompany().getLocation().getCity().equals(filterDTO.getCity())) &&
                (filterDTO.getSkills() == null || filterDTO.getSkills().isEmpty() || vacancy.getSkills().stream().map(Node::getTitle).collect(Collectors.toList()).containsAll(filterDTO.getSkills()));
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