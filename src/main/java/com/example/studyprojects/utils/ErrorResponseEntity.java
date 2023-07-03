package com.example.studyprojects.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public abstract class ErrorResponseEntity {
    LocalDateTime timestamp;
    HttpStatus status;
    String path;
}
