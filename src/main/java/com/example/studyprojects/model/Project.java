package com.example.studyprojects.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Project")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int project_id;

    @NotBlank(message = "Topic should not be blank")
    @Column(name = "topic")
    private String topic;

    @Column(name = "takenAt")
    private LocalDateTime takenAt;

    @Column(name = "rejectedAt")
    private LocalDateTime rejectedAt;

    @Column(name = "mark")
    private int mark;

    @ManyToMany(mappedBy = "projects")
    private Set<Student> group = new HashSet<>();
}
