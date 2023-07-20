package com.example.studyprojects.service;

import com.example.studyprojects.dto.ProjectDto;
import com.example.studyprojects.dto.StudentDto;
import com.example.studyprojects.mapper.StudentMapper;
import com.example.studyprojects.model.Project;
import com.example.studyprojects.model.Role;
import com.example.studyprojects.model.Student;
import com.example.studyprojects.repository.StudentsRepository;
import com.example.studyprojects.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentsService {

    private final StudentsRepository studentsRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentMapper mapper;

    public List<Student> findAllStudents() {
        return studentsRepository.findAll();
    }

    public Student findStudentById(int id) {
        return studentsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Student with id = " + id + " not found")
        );
    }

    public Student findStudentByEmail(String email) {
        return studentsRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User with email " + email + " not found")
        );
    }

    public Student editStudent(StudentDto studentDto, int id) {
        Student student = checkAndGetStudent(id);

        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setGroup(studentDto.getGroup());
        student.setMark(studentDto.getMark());

        return studentsRepository.save(student);
    }

    public Student save(Student student) {
        return studentsRepository.save(student);
    }

    public void deleteStudent(int id) {
        Student student = checkAndGetStudent(id);
        studentsRepository.delete(student);
    }

    private Student checkAndGetStudent(int id) {
        Optional<Student> studToEdit = studentsRepository.findById(id);
        return studToEdit.orElseThrow(
                () -> new NotFoundException("Student with id = " + id + " not found")
        );
    }

    public Student registerStudent(StudentDto studentDto) {
        Student student = mapper.map(studentDto);

        return studentsRepository.save(student.toBuilder()
                        .password(passwordEncoder.encode(student.getPassword()))
                        .active(true)
                        .role(Role.USER)
                        .mark(0)
                        .projects(new HashSet<>())
                .build()
        );
    }

    public List<Project> getStudentsProjects(int id) {
        Student student = studentsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id = " + id + " not found")
        );

        return student.getProjects().stream().toList();

    }
}
