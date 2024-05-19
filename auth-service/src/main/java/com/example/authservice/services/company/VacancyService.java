package com.example.authservice.services.company;

import com.example.authservice.DTOs.company.vacancy.VacancyCreateDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyFilterDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyRecommendedDTO;
import com.example.authservice.DTOs.profile.node.NodeDTO;
import com.example.authservice.models.profile.Node;
import com.example.authservice.models.profile.Profile;
import com.example.authservice.models.profile.WorkExperience;
import com.example.authservice.models.vacancies.Company;
import com.example.authservice.models.vacancies.ExperienceLevel;
import com.example.authservice.models.vacancies.SortType;
import com.example.authservice.models.vacancies.Vacancies;
import com.example.authservice.repositories.company.VacancyRepository;
import com.example.authservice.services.auth.PersonService;
import com.example.authservice.services.profile.NodeService;
import com.example.authservice.services.profile.ProfileService;
import com.example.authservice.services.profile.WorkExperienceService;
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
    private final ProfileService profileService;
    private final WorkExperienceService workExperienceService;
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
    public List<VacancyRecommendedDTO> getFilteredAndSortedVacancies(VacancyFilterDTO filterDTO, String username) {
        List<Vacancies> allVacancies = getAllVacancies();
        List<VacancyRecommendedDTO> sortedVacancies;
        List<VacancyRecommendedDTO> recommendedDTOList = allVacancies.stream()
                .map(vacancy -> new VacancyRecommendedDTO(vacancy, 0))
                .collect(Collectors.toList());
        if(username!=null){
            try{
                RecommendationEngine recommendationEngine = new RecommendationEngine();
                Profile profile = profileService.getProfileByUsername(username);
                List<WorkExperience> workExperiences = workExperienceService.getWorkExperienceByUsername(username);
                recommendedDTOList = recommendationEngine.getRecommendedData(profile,workExperiences,allVacancies);
            } catch (Exception e){
                System.out.println("Ошибка");
            }
        }
            if (filterDTOIsEmpty(filterDTO)) {
                sortedVacancies = recommendedDTOList;
            } else {
                sortedVacancies = sortVacanciesDTO(recommendedDTOList, filterDTO.getSortType());
            }
            if(filterDTO!=null){
                return sortedVacancies.stream()
                        .filter(recommendedDTO -> filterByCriteriaDTO(recommendedDTO, filterDTO))
                        .collect(Collectors.toList());
            }
            else {
                return sortedVacancies;
            }
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
    private List<VacancyRecommendedDTO> sortVacanciesDTO(List<VacancyRecommendedDTO> vacancies, SortType sortType) {
        if (sortType == null) {
            return vacancies;
        }
        switch (sortType) {
            case PRICE_ASCENDING:
                return vacancies.stream()
                        .sorted(Comparator.comparingDouble(vacancy -> vacancy.getVacancy().getSalary().getMinSalary()))
                        .collect(Collectors.toList());
            case PRICE_DESCENDING:
                List<VacancyRecommendedDTO> filteredList = vacancies.stream()
                        .sorted(Comparator.comparingDouble(vacancy -> vacancy.getVacancy().getSalary().getMinSalary()))
                        .collect(Collectors.toList());
                Collections.reverse(filteredList);
                return filteredList;
            case CREATING_DATE_ASCENDING:
                return vacancies.stream()
                        .sorted(Comparator.comparing(vacancy -> vacancy.getVacancy().getCreatingTime()))
                        .collect(Collectors.toList());
            case CREATING_DATE_DESCENDING: {
                List<VacancyRecommendedDTO> sortedList = vacancies.stream()
                        .sorted(Comparator.comparing(vacancy -> vacancy.getVacancy().getCreatingTime()))
                        .collect(Collectors.toList());
                Collections.reverse(sortedList);
                return sortedList;
            }
            default:
                return vacancies;
        }
    }
    private boolean filterByCriteriaDTO(VacancyRecommendedDTO recommendedDTO, VacancyFilterDTO filterDTO) {
        VacancyDTO vacancyDTO = recommendedDTO.getVacancy();
        return (filterDTO.getTitle() == null || filterDTO.getTitle().isEmpty() || vacancyDTO.getTitle().toLowerCase().contains(filterDTO.getTitle().toLowerCase())) &&
                (filterDTO.getMinSalary() == 0 || vacancyDTO.getSalary() != null && vacancyDTO.getSalary().getMinSalary() >= filterDTO.getMinSalary()) &&
                (filterDTO.getExperienceLevel() == null || filterDTO.getExperienceLevel() == ExperienceLevel.NO_FILTER || vacancyDTO.getExperienceLevel() == filterDTO.getExperienceLevel()) &&
                (filterDTO.getCountry() == null || filterDTO.getCountry().isEmpty() || vacancyDTO.getCountry().equalsIgnoreCase(filterDTO.getCountry())) &&
                (filterDTO.getCity() == null || filterDTO.getCity().isEmpty() || vacancyDTO.getCity().equalsIgnoreCase(filterDTO.getCity())) &&
                (filterDTO.getSkills() == null || filterDTO.getSkills().isEmpty() || filterByCriteriaSkillsDTO(recommendedDTO, filterDTO));
    }
    private boolean filterByCriteriaSkillsDTO(VacancyRecommendedDTO recommendedDTO, VacancyFilterDTO filterDTO) {
        VacancyDTO vacancyDTO = recommendedDTO.getVacancy();
        boolean containsAllSkills = vacancyDTO.getSkills().containsAll(filterDTO.getSkills());
        boolean hasMoreSkills = vacancyDTO.getSkills().size() >= filterDTO.getSkills().size();
        return containsAllSkills && hasMoreSkills;
    }
    private VacancyDTO mapVacancyToDTO(Vacancies vacancy) {
        return modelMapper.map(vacancy, VacancyDTO.class);
    }
    private Vacancies mapDTOToVacancy(VacancyCreateDTO vacancyCreateDTO) {
        return modelMapper.map(vacancyCreateDTO, Vacancies.class);
    }
}