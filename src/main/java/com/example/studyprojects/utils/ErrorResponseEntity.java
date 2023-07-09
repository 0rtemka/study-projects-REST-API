package com.example.studyprojects.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public abstract class ErrorResponseEntity {
    LocalDateTime timestamp;
    HttpStatus status;
    String path;
}
