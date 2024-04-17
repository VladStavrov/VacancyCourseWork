package com.example.authservice.services.company;

import com.example.authservice.DTOs.company.vacancy.VacancyCreateDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyFilterDTO;
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

    public List<VacancyDTO> getFilteredAndSortedVacancies(VacancyFilterDTO filterDTO, String username) {
        List<Vacancies> allVacancies = getAllVacancies();
        RecommendationEngine recommendationEngine = new RecommendationEngine();
        List<Vacancies> sortedVacancies;

        if ((filterDTO.getSortType() == null || filterDTO.getSortType() == SortType.RECOMMENDATIONS) && username!=null) {
            Person person = personService.findByUsername(username);
            Profile profile = person.getProfile();
            List<WorkExperience> workExperiences = person.getWorkExperienceList();
            sortedVacancies = recommendationEngine.sortVacanciesByRecommended(profile, workExperiences, allVacancies);
        } else {
            sortedVacancies = sortVacancies(allVacancies, filterDTO.getSortType());
        }

        return sortedVacancies.stream()
                .filter(vacancy -> filterByCriteria(vacancy, filterDTO))
                .map(VacancyDTO::new)
                .collect(Collectors.toList());
    }

    private List<Vacancies> sortVacancies(List<Vacancies> vacancies, SortType sortType) {
        switch (sortType) {
            case PRICE_ASCENDING:
                return vacancies.stream()
                        .sorted(Comparator.comparingDouble(vacancy -> vacancy.getSalary().getMinSalary()))
                        .collect(Collectors.toList());
            case PRICE_DESCENDING:
                List <Vacancies> filteredList =
                vacancies.stream()
                        .sorted(Comparator.comparingDouble(vacancy -> vacancy.getSalary().getMinSalary(), Comparator.reverseOrder()))
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
        // Проверяем каждое поле на пустоту и соответствие критериям
        boolean titleMatch = filterDTO.getTitle() == null || vacancy.getTitle().contains(filterDTO.getTitle());
        boolean minSalaryMatch = filterDTO.getMinSalary() == 0 || vacancy.getSalary() != null && vacancy.getSalary().getMinSalary() >= filterDTO.getMinSalary();
        boolean experienceLevelMatch = filterDTO.getExperienceLevel() == null || vacancy.getExperienceLevel() == filterDTO.getExperienceLevel();
        boolean countryMatch = filterDTO.getCountry() == null || vacancy.getCompany().getLocation().getCountry().equals(filterDTO.getCountry());
        boolean cityMatch = filterDTO.getCity() == null || vacancy.getCompany().getLocation().getCity().equals(filterDTO.getCity());

        return titleMatch && minSalaryMatch && experienceLevelMatch && countryMatch && cityMatch;
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