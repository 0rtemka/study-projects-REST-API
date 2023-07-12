package com.example.studyprojects.service;

import com.example.studyprojects.dto.ProjectThemeDto;
import com.example.studyprojects.model.Group;
import com.example.studyprojects.model.ProjectTheme;
import com.example.studyprojects.repository.ProjectThemesRepository;
import com.example.studyprojects.utils.NotFoundException;
import com.example.studyprojects.utils.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectThemesService {
    private final ProjectThemesRepository repository;

    public void addTheme(ProjectThemeDto theme) {
        validate(theme);
        repository.save(mapFromDto(theme));
    }

    public List<ProjectTheme> findAllThemes() {
        return repository.findAll();
    }

    public List<ProjectTheme> findThemesByGroup(String group) {
        Group groupToFind = checkAndGetGroup(group);
        return repository.findProjectThemesByGroup(groupToFind);
    }

    public ProjectTheme findThemeById(int id) {
        return checkAndGetTheme(id);
    }

    public ProjectTheme editTheme(ProjectThemeDto theme, int id) {
        validate(theme);
        ProjectTheme themeToEdit = checkAndGetTheme(id);

        themeToEdit.setTopic(theme.getTopic());
        themeToEdit.setGroup(theme.getGroup());
        themeToEdit.setAddedAt(LocalDateTime.now());

        repository.save(themeToEdit);
        return themeToEdit;
    }

    public void deleteTheme(int id) {
        ProjectTheme themeToDelete = checkAndGetTheme(id);
        repository.delete(themeToDelete);
    }

    private Group checkAndGetGroup(String group) {
        String groupUpperCase = group.toUpperCase();

        for (Group g : Group.values()) {
            if (g.toString().equals(groupUpperCase)) {
                return g;
            }
        }

        throw new NotFoundException("Group with name '" + groupUpperCase + "' not found");
    }

    private ProjectTheme checkAndGetTheme(int id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Project theme with id = " + id + " not found")
        );
    }

    private void validate(ProjectThemeDto theme) {
        Optional<ProjectTheme> themeWithSameGroup = repository
                .findProjectThemeByTopicAndGroup(theme.getTopic(), theme.getGroup());

        if (themeWithSameGroup.isPresent())
            throw new ValidationException(
                    "Theme '" + theme.getTopic() + "' already exists in " + theme.getGroup() + " group"
            );
    }

    private ProjectTheme mapFromDto(ProjectThemeDto themeDto) {
        return ProjectTheme.builder()
                .topic(themeDto.getTopic())
                .addedAt(themeDto.getAddedAt() == null ? LocalDateTime.now() : themeDto.getAddedAt())
                .group(themeDto.getGroup())
                .build();
    }

    private ProjectThemeDto mapToDto(ProjectTheme theme) {
        return ProjectThemeDto.builder()
                .topic(theme.getTopic())
                .addedAt(theme.getAddedAt() == null ? LocalDateTime.now() : theme.getAddedAt())
                .group(theme.getGroup())
                .build();
    }
}