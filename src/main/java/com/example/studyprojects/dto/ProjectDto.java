package com.example.studyprojects.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class ProjectDto {
    private int project_id;
    @NotBlank(message = "Topic should not be blank")
    private String topic;
    private LocalDateTime takenAt;
    private LocalDateTime expiresAt;
    @Min(value = 1, message = "Mark cannot be less than 1")
    @Max(value = 15, message = "Mark cannot be greater than 15")
    @NotBlank(message = "Mark cannot be blank")
    private int mark;
    private Set<StudentDto> group = new HashSet<>();
}
