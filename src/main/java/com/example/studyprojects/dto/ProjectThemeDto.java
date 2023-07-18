package com.example.studyprojects.dto;

import com.example.studyprojects.model.Group;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectThemeDto {
    private int theme_id;
    @NotBlank(message = "Topic should not be empty")
    private String topic;
    private LocalDateTime addedAt;
    @NotNull(message = "Group can not be null")
    private Group group;
}
