package com.example.studyprojects.controller;

import com.example.studyprojects.dto.ProjectThemeDto;
import com.example.studyprojects.service.ProjectThemesService;
import com.example.studyprojects.utils.ApiError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ProjectThemesController {

    private final ProjectThemesService service;

    @PreAuthorize("hasAuthority('PROJECT_THEMES_READ')")
    @GetMapping
    public List<ProjectThemeDto> findAllThemes() {
        return service.findAllThemes();
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_READ')")
    @GetMapping("/{group}")
    public List<ProjectThemeDto> findThemesByGroup(@PathVariable String group) {
        return service.findThemesByGroup(group);
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_READ')")
    @GetMapping("/id/{id}")
    public ProjectThemeDto findThemeById(@PathVariable int id) {
        return service.findThemeById(id);
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_WRITE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addTheme(@RequestBody @Valid ProjectThemeDto theme, Errors errors) {
        if (errors.hasErrors())
            return new ResponseEntity<>(new ApiError(errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(service.addTheme(theme), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> editTheme(@PathVariable int id, @RequestBody @Valid ProjectThemeDto theme, Errors errors) {
        if (errors.hasErrors())
            return new ResponseEntity<>(new ApiError(errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(service.editTheme(theme, id));
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_WRITE')")
    @DeleteMapping("/{id}")
    public void deleteTheme(@PathVariable int id) {
        service.deleteTheme(id);
    }
}
