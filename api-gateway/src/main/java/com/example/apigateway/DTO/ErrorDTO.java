package com.example.apigateway.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorDTO {

    private int status;
    private String message;
    private Date timestamp;

    public ErrorDTO(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }

}
