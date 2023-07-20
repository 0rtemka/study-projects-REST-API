package com.example.studyprojects.service;

import com.example.studyprojects.model.Project;
import com.example.studyprojects.model.ProjectTheme;
import com.example.studyprojects.model.Student;
import com.example.studyprojects.repository.ProjectsRepository;
import com.example.studyprojects.utils.NotFoundException;
import com.example.studyprojects.utils.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final ProjectThemesService projectThemesService;
    private final StudentsService studentsService;

    public void takeProject(int themeId, Student student) {
        ProjectTheme projectTheme = projectThemesService.findThemeById(themeId);
        isGroupCorrect(projectTheme, student);

        Project project = projectsRepository.findByTopic(projectTheme.getTopic()).orElse(
                Project.builder()
                        .topic(projectTheme.getTopic())
                        .takenAt(LocalDateTime.now())
                        .mark(0)
                        .group(new HashSet<>())
                        .build()
        );

        project.getGroup().add(student);
        student.getProjects().add(project);

        studentsService.save(student);
    }

    private void isGroupCorrect(ProjectTheme projectTheme, Student student) {
        if (!projectTheme.getGroup().equals(student.getGroup())) {
            throw new ValidationException("Student from " + student.getGroup().toString() +
                    " can not take project from " + projectTheme.getGroup().toString() + " group");
        }
    }

    public Project getProjectById(int id) {
        return projectsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Project with id " + id + " not found")
        );
    }

    public List<Project> getAllProjects() {
        return projectsRepository.findAll();
    }
}
