package com.example.studyprojects.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Project")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "group")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;

    @NotBlank(message = "Topic should not be blank")
    @Column(name = "topic")
    private String topic;

    @Column(name = "taken_at")
    private LocalDateTime takenAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "mark")
    private int mark;

    @Column(name = "group_id")
    @Enumerated(EnumType.STRING)
    private Group groupId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH}, mappedBy = "projects")
    private Set<Student> group = new HashSet<>();
}
