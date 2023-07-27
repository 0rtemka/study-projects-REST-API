package com.example.studyprojects.dto;

import com.example.studyprojects.model.Group;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectDto {
    private int projectId;
    private String topic;
    private LocalDateTime takenAt;
    private LocalDateTime expiresAt;
    @Min(value = 1, message = "Mark cannot be less than 1")
    @Max(value = 15, message = "Mark cannot be greater than 15")
    private int mark;
    private Group groupId;
    private Set<StudentDto> group = new HashSet<>();
}
