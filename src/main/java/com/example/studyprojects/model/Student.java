package com.example.studyprojects.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int student_id;

    @NotBlank(message = "Name should not be empty")
    @Column(name = "name")
    private String name;

    @Email(regexp = "stud[0-9]{8}@study\\.ru", message = "Invalid email. Format = 'studXXXXXXXX@study.ru'")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password can not be blank")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Max(value = 100, message = "Mark can not be greater than 100")
    @Min(value = 0, message = "Mark can not be below 0")
    @Column(name = "mark")
    private int mark;

    @NotNull(message = "Group can not be null")
    @Column(name = "group_id")
    @Enumerated(EnumType.STRING)
    private Group group;

    @Column(name = "is_active")
    private boolean active;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(
            name = "Student_Project",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") }
    )
    private Set<Project> projects = new HashSet<>();

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }
}
