package com.example.studyprojects.controller;

import com.example.studyprojects.dto.StudentDto;
import com.example.studyprojects.model.Student;
import com.example.studyprojects.service.StudentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void createStudent(@RequestBody StudentDto student) {
        studentsService.createStudent(student);
    }

    @PutMapping("/{id}")
    public StudentDto editStudent(@PathVariable int id,
                                  @RequestBody StudentDto student)
    {
        return studentsService.editStudent(student, id);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable int id) {
        studentsService.deleteStudent(id);
    }
}
