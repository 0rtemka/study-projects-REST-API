package com.example.studyprojects.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Project_theme")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int theme_id;

    @NotBlank(message = "Topic should not be empty")
    @Column(name = "topic")
    private String topic;

    @Column(name = "addedAt")
    private LocalDateTime addedAt;

    @NotNull(message = "Group can not be null")
    @Column(name = "group_id")
    @Enumerated(EnumType.STRING)
    private Group group;
}
