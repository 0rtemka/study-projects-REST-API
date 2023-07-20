package com.example.studyprojects.repository;

import com.example.studyprojects.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectsRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByTopic(String topic);
}
