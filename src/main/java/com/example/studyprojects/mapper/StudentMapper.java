package com.example.studyprojects.mapper;

import com.example.studyprojects.dto.StudentDto;
import com.example.studyprojects.model.Student;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDto map(Student student);

    @InheritInverseConfiguration
    Student map(StudentDto studentDto);
}
