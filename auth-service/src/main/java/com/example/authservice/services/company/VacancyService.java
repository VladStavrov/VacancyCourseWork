package com.example.authservice.services.company;

import com.example.authservice.DTOs.company.vacancy.VacancyCreateDTO;
import com.example.authservice.DTOs.company.vacancy.VacancyDTO;
import com.example.authservice.models.vacancies.Company;
import com.example.authservice.models.vacancies.Vacancies;
import com.example.authservice.repositories.company.VacancyRepository;
import com.example.authservice.services.auth.PersonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacancyService {


    private final VacancyRepository vacancyRepository;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;
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
        vacancy.setCompany(company);
        Vacancies savedVacancy = vacancyRepository.save(vacancy);
        return new VacancyDTO(savedVacancy);
    }

    public VacancyDTO updateVacancy(Long id, VacancyCreateDTO vacancyCreateDTO) {
        Vacancies existingVacancy = vacancyRepository.findById(id).orElse(null);

        if (existingVacancy != null) {
            if (vacancyCreateDTO.getTitle() != null) {
                existingVacancy.setTitle(vacancyCreateDTO.getTitle());
            }
            if (vacancyCreateDTO.getSalary() != null) {
                existingVacancy.setSalary(vacancyCreateDTO.getSalary());
            }
            if (vacancyCreateDTO.getExperienceLevel() != null) {
                existingVacancy.setExperienceLevel(vacancyCreateDTO.getExperienceLevel());
            }
            if (vacancyCreateDTO.getDescription() != null) {
                existingVacancy.setDescription(vacancyCreateDTO.getDescription());
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