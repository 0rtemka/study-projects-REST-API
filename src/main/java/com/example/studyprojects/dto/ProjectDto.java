package com.example.studyprojects.dto;

import com.example.studyprojects.model.Student;
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
    private LocalDateTime rejectedAt;
    private int mark;
    private Set<StudentDto> group = new HashSet<>();
}
