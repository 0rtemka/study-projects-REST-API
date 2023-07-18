package com.example.studyprojects.security;

import com.example.studyprojects.model.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record StudentDetails(Student student) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return student.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return student.getPassword();
    }

    @Override
    public String getUsername() {
        return student.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return student.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return student.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return student.isActive();
    }

    @Override
    public boolean isEnabled() {
        return student.isActive();
    }
}
