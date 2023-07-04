package com.example.studyprojects.service;

import com.example.studyprojects.dto.StudentDto;
import com.example.studyprojects.model.Student;
import com.example.studyprojects.repository.StudentsRepository;
import com.example.studyprojects.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentsService {
    private final StudentsRepository studentsRepository;

    public List<Student> findAllStudents() {
        return studentsRepository.findAll();
    }

    public Student findStudentById(int id) {
        return studentsRepository.findById(id).orElseThrow(
                () -> {throw new NotFoundException("Student with id = " + id  + " not found");
                });
    }

    public void createStudent(StudentDto student) {
        studentsRepository.save(
                mapFromDto(student)
        );
    }

    private Student mapFromDto(StudentDto studentDto) {
        return Student.builder()
                .name(studentDto.getName())
                .email(studentDto.getEmail())
                .mark(studentDto.getMark())
                .group(studentDto.getGroup())
                .build();
    }

    public StudentDto editStudent(StudentDto studentDto, int id) {
        Student student = checkStudent(id);

        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setGroup(studentDto.getGroup());
        student.setMark(studentDto.getMark());

        studentsRepository.save(student);
        return studentDto;
    }

    public void deleteStudent(int id) {
        Student student = checkStudent(id);
        studentsRepository.delete(student);
    }

    private Student checkStudent(int id) {
        Optional<Student> studToEdit = studentsRepository.findById(id);
        if (studToEdit.isEmpty()) throw new NotFoundException("Student with id = " + id + " not found");
        return studToEdit.get();
    }
}
