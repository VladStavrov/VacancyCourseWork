package com.example.authservice.services.company;


import com.example.authservice.DTOs.company.company.CompanyCreateDTO;
import com.example.authservice.DTOs.company.company.CompanyDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.models.vacancies.Company;
import com.example.authservice.repositories.company.CompanyRepository;
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
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final PersonService personService;


    public List<CompanyDTO> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream().map(CompanyDTO::new).collect(Collectors.toList());
    }


    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
    }

    public CompanyDTO getCompanyDTOById(Long id) {
        Company company = getCompanyById(id);
        return new CompanyDTO(company);

    }

    public CompanyDTO createCompany(CompanyCreateDTO companyCreateDTO, String username) {
        Person person = personService.findByUsername(username);
        Company company = mapDTOToCompany(companyCreateDTO);
        company.setPerson(person);
        Company savedCompany = companyRepository.save(company);
        return new CompanyDTO(savedCompany);
    }

    public CompanyDTO updateCompany(Long id, CompanyCreateDTO companyCreateDTO) {
        Company existingCompany = companyRepository.findById(id).orElse(null);

        if (existingCompany != null) {
            if (companyCreateDTO.getCompanyName() != null) {
                existingCompany.setCompanyName(companyCreateDTO.getCompanyName());
            }
            if (companyCreateDTO.getLocation() != null) {
                existingCompany.setLocation(companyCreateDTO.getLocation());
            }
            if (companyCreateDTO.getDescription() != null) {
                existingCompany.setDescription(companyCreateDTO.getDescription());
            }
            if (companyCreateDTO.getEmail() != null) {
                existingCompany.setEmail(companyCreateDTO.getEmail());
            }
            if (companyCreateDTO.getPhoneNumber() != null) {
                existingCompany.setPhoneNumber(companyCreateDTO.getPhoneNumber());
            }

            Company updatedCompany = companyRepository.save(existingCompany);
            return mapCompanyToDTO(updatedCompany);
        }

        return null;
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    private CompanyDTO mapCompanyToDTO(Company company) {
        return modelMapper.map(company, CompanyDTO.class);
    }

    private Company mapDTOToCompany(CompanyDTO companyDTO) {
        return modelMapper.map(companyDTO, Company.class);
    }

    private Company mapDTOToCompany(CompanyCreateDTO companyCreateDTO) {
        return modelMapper.map(companyCreateDTO, Company.class);
    }
}
