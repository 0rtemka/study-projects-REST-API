package com.example.studyprojects.controller;

import com.example.studyprojects.dto.StudentDto;
import com.example.studyprojects.service.StudentsService;
import com.example.studyprojects.utils.ApiError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsService;

    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    @GetMapping
    public List<StudentDto> findAllStudents() {
        return studentsService.findAllStudents();
    }

    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    @GetMapping("/{id}")
    public StudentDto findStudentById(@PathVariable int id) {
        return studentsService.findStudentById(id);
    }

    @PreAuthorize("hasAuthority('STUDENTS_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> editStudent(@PathVariable int id,
                                  @RequestBody @Valid StudentDto student, Errors errors)
    {
        if (errors.hasErrors()) {
            if (errors.getFieldErrors().size() > 1 && !errors.hasFieldErrors("password"))
                return new ResponseEntity<>(new ApiError(errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(studentsService.editStudent(student, id));
    }

    @PreAuthorize("hasAuthority('STUDENTS_WRITE')")
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable int id) {
        studentsService.deleteStudent(id);
    }
}
