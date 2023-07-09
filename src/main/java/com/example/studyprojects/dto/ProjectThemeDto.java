package com.example.studyprojects.dto;

import com.example.studyprojects.model.Group;
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
    private String topic;

    private LocalDateTime addedAt;

    private Group group;
}
