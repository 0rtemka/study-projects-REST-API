package com.example.studyprojects.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponseEntity {
    private String message;
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String path;
}
