package com.example.studyprojects.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class ApiError {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private Map<String, String> errors = new HashMap<>();

    public ApiError(Errors errors, HttpStatus status) {
        errors.getFieldErrors().forEach(error -> {
            this.errors.put(error.getField(), error.getDefaultMessage());
        });
        timestamp = LocalDateTime.now();
        this.status = status;
    }
}
