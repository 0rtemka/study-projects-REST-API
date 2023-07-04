package com.example.studyprojects.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Student")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int student_id;

    @NotBlank(message = "Name should not be empty")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Email should not be empty")
    @Email(regexp = "stud[0-9]{8}@study\\.ru", message = "Invalid email. Format = 'studXXXXXXXX@study.ru'")
    @Column(name = "email")
    private String email;

    @Max(value = 100, message = "Mark can not be greater than 100")
    @Min(value = 0, message = "Mark can not be below 0")
    @Column(name = "mark")
    private int mark;

    @Column(name = "group_id")
    @Enumerated(EnumType.STRING)
    private Group group;

    @ManyToMany
    @JoinTable(
            name = "Student_Project",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") }
    )
    private Set<Project> projects = new HashSet<>();
}
