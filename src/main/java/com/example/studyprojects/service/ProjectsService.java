package com.example.studyprojects.service;

import com.example.studyprojects.model.Group;
import com.example.studyprojects.model.Project;
import com.example.studyprojects.model.ProjectTheme;
import com.example.studyprojects.model.Student;
import com.example.studyprojects.repository.ProjectsRepository;
import com.example.studyprojects.utils.NotFoundException;
import com.example.studyprojects.utils.TakeProjectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final ProjectThemesService projectThemesService;
    private final StudentsService studentsService;

    @Transactional
    public void takeProject(int themeId, Student student) {
        ProjectTheme projectTheme = projectThemesService.findAvailableThemeById(themeId);
        isGroupCorrect(projectTheme, student);

        Project project = projectsRepository.findByTopicAndExpiresAtAfter(projectTheme.getTopic(), LocalDateTime.now())
                .orElse(Project.builder()
                        .topic(projectTheme.getTopic())
                        .takenAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plus(30, ChronoUnit.SECONDS))
                        .mark(0)
                        .groupId(projectTheme.getGroup())
                        .group(new HashSet<>())
                        .build()
                );

        if (project.getGroup().size() == 3) {
            throw new TakeProjectException("Project " + project.getTopic() + " has maximum size of group (3 people)");
        }

        project.getGroup().add(student);
        student.getProjects().add(project);

        studentsService.save(student);
    }

    @Transactional(readOnly = true)
    public Project getProjectById(int id) {
        return projectsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Project with id " + id + " not found")
        );
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects(String sortBy, String group, int page, int size, List<String> status) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        List<Project> projects = switch(group.toLowerCase()) {
            case "all" -> projectsRepository.findAll(pageRequest).getContent();
            case "is" -> projectsRepository.findAllByGroupId(Group.IS, pageRequest);
            case "isas" -> projectsRepository.findAllByGroupId(Group.ISAS, pageRequest);
            case "cs" -> projectsRepository.findAllByGroupId(Group.CS, pageRequest);
            default -> throw new NotFoundException("Group with name '" + group.toUpperCase() + "' not found");
        };

        for (String filter : status) {
            projects = switch (filter.toUpperCase()) {
                case "ALL" -> projects;
                case "MARKED" -> projects.stream().filter(p -> p.getMark() != 0).toList();
                case "UNMARKED" -> projects.stream().filter(p -> p.getMark() == 0).toList();
                case "ACTIVE" -> projects.stream().filter(p -> p.getExpiresAt().isAfter(LocalDateTime.now())).toList();
                case "INACTIVE" -> projects.stream().filter(p -> p.getExpiresAt().isBefore(LocalDateTime.now())).toList();
                default -> throw new NotFoundException("Illegal status: " + filter);
            };
        }

        return projects;
    }

    @Transactional(readOnly = true)
    public List<Project> getAllAvailableProjects() {
        return projectsRepository.findAllByExpiresAtAfter(LocalDateTime.now());
    }

    @Transactional
    public void deleteProject(int id) {
        Project project = projectsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Project with id " + id + " not found")
        );

        for (Student s : project.getGroup()) {
            s.getProjects().remove(project);
        }

        projectsRepository.delete(project);
    }

    private void isGroupCorrect(ProjectTheme projectTheme, Student student) {
        if (!projectTheme.getGroup().equals(student.getGroup())) {
            throw new TakeProjectException("Student from " + student.getGroup() +
                    " can not take project from " + projectTheme.getGroup() + " group");
        }
    }

    @Transactional
    public void rateProject(int mark, int projectId) {
        Project project = projectsRepository.findById(projectId).orElseThrow(
                () -> new NotFoundException("Project with id = " + projectId + " not found")
        );
        ProjectTheme theme = projectThemesService.findThemeByTopicAndGroup(
                project.getTopic(),
                project.getGroup().stream().findFirst().get().getGroup()
        );

        projectThemesService.setThemeUnavailable(theme);
        project.getGroup().forEach(s -> s.setMark(s.getMark() + mark));

        project.setMark(mark);

        projectsRepository.save(project);
    }
}
