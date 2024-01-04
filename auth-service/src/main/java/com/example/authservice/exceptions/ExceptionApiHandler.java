package com.example.authservice.exceptions;

import com.example.authservice.DTOs.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(LocalException.class)
    public ResponseEntity<?> notFoundException(LocalException exception) {
        return new ResponseEntity<>(new ErrorDTO(exception.getBody().getStatus(),exception.getReason()), exception.getStatusCode());
    }

}