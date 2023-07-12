package com.example.studyprojects.controller;

import com.example.studyprojects.dto.ProjectThemeDto;
import com.example.studyprojects.model.ProjectTheme;
import com.example.studyprojects.service.ProjectThemesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ProjectThemesController {

    private final ProjectThemesService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTheme(@Valid @RequestBody ProjectThemeDto theme) {
        service.addTheme(theme);
    }

    @GetMapping
    public List<ProjectTheme> findAllThemes() {
        return service.findAllThemes();
    }

    @GetMapping("/{group}")
    public List<ProjectTheme> findThemesByGroup(@PathVariable String group) {
        return service.findThemesByGroup(group);
    }

    @GetMapping("/id/{id}")
    public ProjectTheme findThemeById(@PathVariable int id) {
        return service.findThemeById(id);
    }

    @PutMapping("/{id}")
    public ProjectTheme editTheme(@PathVariable int id, @Valid @RequestBody ProjectThemeDto theme) {
        return service.editTheme(theme, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTheme(@PathVariable int id) {
        service.deleteTheme(id);
    }


}
