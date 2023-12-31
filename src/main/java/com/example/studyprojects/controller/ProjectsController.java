package com.example.studyprojects.controller;

import com.example.studyprojects.dto.ProjectDto;
import com.example.studyprojects.mapper.ProjectMapper;
import com.example.studyprojects.security.StudentDetails;
import com.example.studyprojects.service.ProjectsService;
import com.example.studyprojects.utils.ApiError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectsController {

    private final ProjectsService projectsService;
    private final ProjectMapper mapper;

    @PreAuthorize("hasAuthority('PROJECTS_READ')")
    @PostMapping("/{themeId}")
    public void takeProject(@PathVariable int themeId, @AuthenticationPrincipal StudentDetails student) {
        projectsService.takeProject(themeId, student.student());
    }

    @PreAuthorize("hasAuthority('PROJECTS_WRITE')")
    @PutMapping("/{projectId}")
    public ResponseEntity<Object> rateProject(@PathVariable int projectId, @RequestBody @Valid ProjectDto projectDto, Errors errors) {
        if (errors.hasErrors())
            return new ResponseEntity<>(new ApiError(errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(projectsService.rateProject(projectDto.getMark(), projectId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PROJECTS_READ')")
    @GetMapping
    public List<ProjectDto> getAllProjects(
            @RequestParam(name = "status", required = false, defaultValue = "all") List<String> status,
            @RequestParam(name = "group", required = false, defaultValue = "all") String group,
            @RequestParam(name = "sortBy", required = false, defaultValue = "projectId") String sortBy,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size)
    {
        return projectsService.getAllProjects(sortBy, group, page, size, status).stream()
                .map(mapper::map)
                .toList();
    }

    @PreAuthorize("hasAuthority('PROJECTS_READ')")
    @GetMapping("/{id}")
    public ProjectDto getProjectById(@PathVariable int id) {
        return mapper.map(projectsService.getProjectById(id));
    }

    @PreAuthorize("hasAuthority('PROJECTS_WRITE')")
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable int id) {
        projectsService.deleteProject(id);
    }
}
