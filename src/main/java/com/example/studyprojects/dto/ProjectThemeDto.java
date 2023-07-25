package com.example.studyprojects.dto;

import com.example.studyprojects.model.Group;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectThemeDto {
    private int themeId;
    @NotBlank(message = "Topic should not be empty")
    private String topic;
    private LocalDateTime addedAt;
    private boolean available;
    @NotNull(message = "Group can not be null")
    private Group group;
}
