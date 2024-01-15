package com.example.authservice.exceptions;

import com.example.authservice.DTOs.auth.ErrorDTO;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ExceptionApiHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionApiHandler.class);

    @ExceptionHandler(LocalException.class)
    public ResponseEntity<?> notFoundException(LocalException exception) {
        return new ResponseEntity<>(new ErrorDTO(exception.getBody().getStatus(),exception.getReason()), exception.getStatusCode());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        logger.error("ResponseStatusException: {}", ex.getReason());
        String simpleMessage = "Error: " + ex.getReason();
        return ResponseEntity.status(ex.getStatusCode()).body(simpleMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("DataIntegrityViolationException: {}", ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage());
        String simpleMessage = "Data integrity violation. Please check your request details.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(simpleMessage);
    }

    /*@ExceptionHandler( ServletException.class)
    public ResponseEntity<String> handleServletException( ServletException ex) {
        logger.error("ServletException: {}",ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage());
        String simpleMessage = "Error :{}"+ (ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(simpleMessage);
    }*/
    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unhandled Exception: {}", ex.getClass().getSimpleName());
        String simpleMessage = "Internal server error. Please contact support.";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(simpleMessage);
    }*/
}