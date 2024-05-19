package com.example.authservice.services.company;
import com.example.authservice.DTOs.company.response.ResponseCreateDTO;
import com.example.authservice.DTOs.company.response.ResponseDTO;
import com.example.authservice.models.auth.Person;
import com.example.authservice.models.profile.Profile;
import com.example.authservice.models.vacancies.Company;
import com.example.authservice.models.vacancies.Response;
import com.example.authservice.models.vacancies.Vacancies;
import com.example.authservice.repositories.company.ResponseRepository;
import com.example.authservice.services.auth.PersonService;
import com.example.authservice.services.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final PersonService personService;
    private final VacancyService vacancyService;
    public Response getResponseById(Long id) {
        return responseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id));
    }
    public ResponseDTO getResposeDTOById(Long id){
        Response response = getResponseById(id);
        return new ResponseDTO(response);
    }
    public List<ResponseDTO> getAllResponsesByVacancyId(Long vacancyId) {
        List<Response> responses = responseRepository.findAllByVacancyId(vacancyId);
        return responses.stream()
                .map(ResponseDTO::new)
                   .collect(Collectors.toList());
    }
    public List<ResponseDTO> getAllResponsesByUsername(String username) {
        List<Response> responses = responseRepository.findAllByPersonUsername(username);
        return responses.stream()
                .map(ResponseDTO::new)
                .collect(Collectors.toList());
    }
    public List<ResponseDTO> getAllResponsesByCompanyName(String username) {
        List<Response> responses = responseRepository.findAllByVacancyCompanyPersonUsername(username);
        return responses.stream()
                .map(ResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ResponseDTO createResponse(ResponseCreateDTO responseDTO) {
        Optional<Response> responseTest = responseRepository.findByPersonUsernameAndVacancyId(responseDTO.getUsername(),responseDTO.getVacancyId());
        if(responseTest.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Company with this username and id is present: ");
        }
        Person person = personService.findByUsername(responseDTO.getUsername());
        Vacancies vacancy = vacancyService.getVacancyById(responseDTO.getVacancyId());
        Response response = new Response();
        response.setPerson(person);
        response.setVacancy(vacancy);
        response.setStatus(responseDTO.getStatus());
        Response savedResponse = responseRepository.save(response);
        return new ResponseDTO(savedResponse);
    }
    public ResponseDTO updateResponseStatus(Long responseId, String newStatus) {
        Response response = getResponseById(responseId);
        response.setStatus(newStatus);
        Response updatedResponse = responseRepository.save(response);
        return new ResponseDTO(updatedResponse);
    }
    @Transactional
    public void deleteResponseById(Long id){
        responseRepository.deleteById(id);
    }
}