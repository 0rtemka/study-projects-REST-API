package com.example.studyprojects.controller;

import com.example.studyprojects.dto.StudentDto;
import com.example.studyprojects.service.StudentsService;
import com.example.studyprojects.utils.ApiError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final StudentsService studentsService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid StudentDto studentDto, Errors errors) {
        if (errors.hasErrors())
            return new ResponseEntity<>(new ApiError(errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(studentsService.registerStudent(studentDto));
    }
}
