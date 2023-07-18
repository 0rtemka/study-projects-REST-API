package com.example.studyprojects.model;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public enum Role {
    USER(Set.of(new SimpleGrantedAuthority("PROJECT_THEMES_READ"),
                new SimpleGrantedAuthority("STUDENTS_READ"))),
    ADMIN(Set.of(new SimpleGrantedAuthority("PROJECT_THEMES_READ"),
                 new SimpleGrantedAuthority("PROJECT_THEMES_WRITE"),
                 new SimpleGrantedAuthority("STUDENTS_READ"),
                 new SimpleGrantedAuthority("STUDENTS_WRITE")));

    @Getter
    private final Set<SimpleGrantedAuthority> authorities;

    Role(Set<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
