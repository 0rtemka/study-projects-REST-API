package com.example.studyprojects.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Project_theme")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectTheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int theme_id;

    @Column(name = "topic")
    private String topic;

    @Column(name = "group_id")
    @Enumerated(EnumType.STRING)
    private Group group;
}
