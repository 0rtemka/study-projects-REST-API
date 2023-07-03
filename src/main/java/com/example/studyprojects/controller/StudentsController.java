package com.example.studyprojects.controller;

import com.example.studyprojects.dto.StudentDto;
import com.example.studyprojects.model.Student;
import com.example.studyprojects.service.StudentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentsController {

    private final StudentsService studentsService;

    @GetMapping
    public List<Student> findAllStudents() {
        return studentsService.findAllStudents();
    }

    @GetMapping("/{id}")
    public Student findStudentById(@PathVariable int id) {
        return studentsService.findStudentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createStudent(@Valid @RequestBody StudentDto student) {
        studentsService.createStudent(student);
    }

    @PutMapping("/{id}")
    public StudentDto editStudent(@PathVariable int id,
                                  @Valid @RequestBody StudentDto student)
    {
        return studentsService.editStudent(student, id);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable int id) {
        studentsService.deleteStudent(id);
    }
}
