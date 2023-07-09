package com.example.studyprojects.controller;

import com.example.studyprojects.utils.ErrorResponseEntity;
import com.example.studyprojects.utils.MainErrorResponseEntity;
import com.example.studyprojects.utils.NotFoundException;
import com.example.studyprojects.utils.ValidationErrorResponseEntity;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> notFoundHandler(RuntimeException e, WebRequest r) {
        MainErrorResponseEntity response = MainErrorResponseEntity.builder()
                .message(e.getMessage()).build();

        mapToErrorResponseEntity(response, HttpStatus.NOT_FOUND, r);

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(ConstraintViolationException e, WebRequest r)  {
        Map<String, String> errors = new HashMap<>();

        e.getConstraintViolations().forEach((error) -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponseEntity response = ValidationErrorResponseEntity.builder()
                .errors(errors).build();
        mapToErrorResponseEntity(response, HttpStatus.BAD_REQUEST, r);

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }

    private void mapToErrorResponseEntity(ErrorResponseEntity entity, HttpStatus status, WebRequest r) {
        entity.setTimestamp(LocalDateTime.now());
        entity.setStatus(status);
        entity.setPath(((ServletWebRequest)r).getRequest().getRequestURI().toString());
    }
}
