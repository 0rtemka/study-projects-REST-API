package com.example.studyprojects.security;

import com.example.studyprojects.model.Student;
import com.example.studyprojects.service.StudentsService;
import com.example.studyprojects.utils.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentsAuthenticationManager implements AuthenticationManager {

    private final StudentsService studentsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Student student = studentsService.findStudentByEmail(authentication.getName());
        if (!student.isActive()) throw new AuthException("User is disabled");
        return (Authentication) student;
    }
}
