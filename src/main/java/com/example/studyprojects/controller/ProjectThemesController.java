package com.example.studyprojects.controller;

import com.example.studyprojects.dto.ProjectThemeDto;
import com.example.studyprojects.mapper.ProjectThemeMapper;
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
    private final ProjectThemeMapper mapper;

    @PreAuthorize("hasAuthority('PROJECT_THEMES_READ')")
    @GetMapping
    public List<ProjectThemeDto> findAllThemes() {
        return service.findAllAvailableThemes().stream()
                .map(mapper::map)
                .toList();
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_READ')")
    @GetMapping("/{group}")
    public List<ProjectThemeDto> findThemesByGroup(@PathVariable String group) {
        return service.findThemesByGroup(group).stream()
                .map(mapper::map)
                .toList();
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_READ')")
    @GetMapping("/id/{id}")
    public ProjectThemeDto findThemeById(@PathVariable int id) {
        return mapper.map(service.findThemeById(id));
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_WRITE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addTheme(@RequestBody @Valid ProjectThemeDto theme, Errors errors) {
        if (errors.hasErrors())
            return new ResponseEntity<>(new ApiError(errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(mapper.map(service.addTheme(theme)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> editTheme(@PathVariable int id, @RequestBody @Valid ProjectThemeDto theme, Errors errors) {
        if (errors.hasErrors())
            return new ResponseEntity<>(new ApiError(errors, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(mapper.map(service.editTheme(theme, id)));
    }

    @PreAuthorize("hasAuthority('PROJECT_THEMES_WRITE')")
    @DeleteMapping("/{id}")
    public void deleteTheme(@PathVariable int id) {
        service.deleteTheme(id);
    }
}
