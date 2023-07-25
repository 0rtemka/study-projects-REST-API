package com.example.studyprojects.repository;

import com.example.studyprojects.model.Group;
import com.example.studyprojects.model.Project;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProjectsRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByTopic(String topic);

    Optional<Project> findByTopicAndExpiresAtAfter(String topic, LocalDateTime time);

    List<Project> findAllByExpiresAtAfter(LocalDateTime time);

    List<Project> findAllByGroupId(Group group, PageRequest pageRequest);
}
