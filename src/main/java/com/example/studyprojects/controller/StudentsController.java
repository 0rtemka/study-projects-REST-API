package com.example.studyprojects.controller;

import com.example.studyprojects.dto.ProjectDto;
import com.example.studyprojects.dto.StudentDto;
import com.example.studyprojects.mapper.ProjectMapper;
import com.example.studyprojects.mapper.StudentMapper;
import com.example.studyprojects.model.Group;
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
    private final StudentMapper studentMapper;
    private final ProjectMapper projectMapper;

    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    @GetMapping
    public List<StudentDto> findAllStudents(
            @RequestParam(name = "sortBy", required = false, defaultValue = "studentId") String sortBy,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "group", required = false, defaultValue = "all") String group)
    {
        return studentsService.findAllStudentsSortAndPagination(sortBy, page, size, group).stream()
                .map(studentMapper::map)
                .toList();
    }

    @PreAuthorize("hasAuthority('STUDENTS_READ')")
    @GetMapping("/{id}")
    public StudentDto findStudentById(@PathVariable int id) {
        return studentMapper.map(studentsService.findStudentById(id));
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
        return ResponseEntity.ok(studentMapper.map(studentsService.editStudent(student, id)));
    }

    @PreAuthorize("hasAuthority('STUDENTS_WRITE')")
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable int id) {
        studentsService.deleteStudent(id);
    }

    @GetMapping("/{id}/projects")
    public List<ProjectDto> getStudentsProjects(
            @PathVariable int id,
            @RequestParam(name = "status", required = false, defaultValue = "all") List<String> status,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size)
    {
        return studentsService.getStudentsProjects(id, status, page, size).stream()
                .map(projectMapper::map)
                .toList();
    }
}
