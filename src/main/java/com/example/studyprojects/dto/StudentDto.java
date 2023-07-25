package com.example.studyprojects.dto;

import com.example.studyprojects.model.Group;
import com.example.studyprojects.model.Project;
import com.example.studyprojects.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudentDto {
    private int studentId;
    @NotBlank(message = "Name should not be empty")
    private String name;
    @Email(regexp = "stud[0-9]{8}@study\\.ru", message = "Invalid email. Format = 'studXXXXXXXX@study.ru'")
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password can not be blank")
    private String password;
    private boolean active;
    private Role role;
    @NotNull(message = "Group can not be null")
    private Group group;
    @Max(value = 100, message = "Mark can not be greater than 100")
    @Min(value = 0, message = "Mark can not be below 0")
    private int mark;
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();
}
