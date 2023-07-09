package com.example.studyprojects.service;

import com.example.studyprojects.dto.ProjectThemeDto;
import com.example.studyprojects.model.Group;
import com.example.studyprojects.model.ProjectTheme;
import com.example.studyprojects.repository.ProjectThemesRepository;
import com.example.studyprojects.utils.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectThemesService {
    private final ProjectThemesRepository repository;

    public void addTheme(ProjectThemeDto theme) {
        repository.save(mapFromDto(theme));
    }

    public List<ProjectTheme> findAllProjects() {
        return repository.findAll();
    }

    public List<ProjectTheme> findThemesByGroup(String group) {
        Group groupToFind = checkAndGetGroup(group);

        return repository.findProjectThemesByGroup(groupToFind);
    }

    public ProjectTheme editTheme(ProjectThemeDto theme, int id) {
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

        throw new NotFoundException("Group with name '" + group + "' not found");
    }

    private ProjectTheme checkAndGetTheme(int id) {

        //TODO: Check if same topic but different id

        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Project theme with id = " + id + " not found")
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
