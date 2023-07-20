package com.example.studyprojects.mapper;

import com.example.studyprojects.dto.ProjectDto;
import com.example.studyprojects.model.Project;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDto map(Project project);

    @InheritInverseConfiguration
    Project map(ProjectDto projectDto);
}
