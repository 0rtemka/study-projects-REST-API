package com.example.studyprojects.controller;

import com.example.studyprojects.utils.ErrorResponseEntity;
import com.example.studyprojects.utils.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> notFoundHandler(RuntimeException e, WebRequest r) {
        ErrorResponseEntity response = new ErrorResponseEntity(
                e.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                ((ServletWebRequest)r).getRequest().getRequestURI().toString()
        );

        return handleExceptionInternal(e, response, new HttpHeaders(),
                HttpStatusCode.valueOf(404), r);
    }
}
