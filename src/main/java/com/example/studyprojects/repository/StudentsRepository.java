package com.example.studyprojects.repository;

import com.example.studyprojects.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentsRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByEmail(String email);
}
