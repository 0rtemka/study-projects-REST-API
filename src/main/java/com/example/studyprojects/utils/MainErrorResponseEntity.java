package com.example.studyprojects.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MainErrorResponseEntity extends ErrorResponseEntity {
    private String message;
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String path;
}
