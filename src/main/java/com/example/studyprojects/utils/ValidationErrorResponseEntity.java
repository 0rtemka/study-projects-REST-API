package com.example.studyprojects.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationErrorResponseEntity extends ErrorResponseEntity {
    private Map<String, String> errors;
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String path;
}
