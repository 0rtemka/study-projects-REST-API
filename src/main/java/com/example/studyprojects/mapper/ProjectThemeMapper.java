package com.example.studyprojects.mapper;

import com.example.studyprojects.dto.ProjectThemeDto;
import com.example.studyprojects.model.ProjectTheme;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectThemeMapper {
    ProjectThemeDto map(ProjectTheme projectTheme);

    @InheritInverseConfiguration
    ProjectTheme map(ProjectThemeDto projectThemeDto);
}
