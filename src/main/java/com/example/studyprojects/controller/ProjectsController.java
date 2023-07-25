package com.example.studyprojects.controller;

import com.example.studyprojects.dto.ProjectDto;
import com.example.studyprojects.mapper.ProjectMapper;
import com.example.studyprojects.security.StudentDetails;
import com.example.studyprojects.service.ProjectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectsController {

    private final ProjectsService projectsService;
    private final ProjectMapper mapper;

    @PostMapping("/{themeId}")
    public void takeProject(@PathVariable int themeId, @AuthenticationPrincipal StudentDetails student) {
        projectsService.takeProject(themeId, student.student());
    }

    @PutMapping("/{projectId}")
    public void rateProject(@PathVariable int projectId, @RequestBody ProjectDto projectDto) {
        projectsService.rateProject(projectDto.getMark(), projectId);
    }

    @GetMapping
    public List<ProjectDto> getAllProjects() {
        return projectsService.getAllAvailableProjects().stream()
                .map(mapper::map)
                .toList();
    }

    @GetMapping("/{id}")
    public ProjectDto getProjectById(@PathVariable int id) {
        return mapper.map(projectsService.getProjectById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable int id) {
        projectsService.deleteProject(id);
    }
}
