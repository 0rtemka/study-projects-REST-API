package com.example.studyprojects.repository;

import com.example.studyprojects.model.Group;
import com.example.studyprojects.model.ProjectTheme;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectThemesRepository extends JpaRepository<ProjectTheme, Integer> {
    List<ProjectTheme> findProjectThemesByGroup(Group group);

    List<ProjectTheme> findProjectThemesByAvailableIsTrue();

    List<ProjectTheme> findProjectThemesByGroupAndAvailableIsTrue(Group group, PageRequest pageRequest);

    List<ProjectTheme> findProjectThemesByAvailableIsTrue(PageRequest pageRequest);

    Optional<ProjectTheme> findProjectThemeByTopicAndGroup(String topic, Group group);

    Optional<ProjectTheme> findByThemeIdAndAvailableIsTrue(int id);

}
