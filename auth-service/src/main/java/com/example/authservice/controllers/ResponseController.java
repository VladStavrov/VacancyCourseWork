package com.example.authservice.controllers;

import com.example.authservice.DTOs.company.response.ResponseDTO;
import com.example.authservice.services.company.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {

    private final ResponseService responseService;

    @Autowired
    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @GetMapping("/{id}")
    public ResponseDTO getResponseById(@PathVariable Long id) {
        return responseService.getResposeDTOById(id);
    }

    @GetMapping("/vacancy/{vacancyId}")
    public List<ResponseDTO> getAllResponsesByVacancyId(@PathVariable Long vacancyId) {
        return responseService.getAllResponsesByVacancyId(vacancyId);
    }

    @GetMapping("/username/{username}")
    public List<ResponseDTO> getAllResponsesByUsername(@PathVariable String username) {
        return responseService.getAllResponsesByUsername(username);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO createResponse(@RequestBody ResponseDTO responseDTO) {
        return responseService.createResponse(responseDTO);
    }

    @PutMapping("/{id}/updateStatus")
    public ResponseDTO updateResponseStatus(@PathVariable Long id, @RequestParam String newStatus) {
        return responseService.updateResponseStatus(id, newStatus);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResponseById(@PathVariable Long id) {
        responseService.deleteResponseById(id);
    }
}