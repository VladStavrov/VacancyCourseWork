package com.example.treeservice.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomGlobalExceptionHandler.class);

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

   /* @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        logger.error("Unhandled Exception: {}", ex.getClass().getSimpleName());
        String simpleMessage = "Internal server error. Please contact support.";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(simpleMessage);
    }*/

}
