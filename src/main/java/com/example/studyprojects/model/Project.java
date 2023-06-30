package com.example.studyprojects.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Project")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int project_id;

    private String topic;

    @Column(name = "takenAt")
    private LocalDateTime takenAt;

    @Column(name = "mark")
    private int mark;

    @ManyToMany(mappedBy = "projects")
    private Set<Student> group;

}
