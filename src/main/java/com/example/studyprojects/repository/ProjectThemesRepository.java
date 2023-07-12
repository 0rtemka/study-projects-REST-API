package com.example.studyprojects.repository;

import com.example.studyprojects.model.Group;
import com.example.studyprojects.model.ProjectTheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectThemesRepository extends JpaRepository<ProjectTheme, Integer> {
    List<ProjectTheme> findProjectThemesByGroup(Group group);
    Optional<ProjectTheme> findProjectThemeByTopicAndGroup(String topic, Group group);
}
