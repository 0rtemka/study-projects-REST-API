package com.example.studyprojects.security;

import com.example.studyprojects.repository.StudentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentDetailsService implements UserDetailsService {

    private final StudentsRepository studentsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new StudentDetails(studentsRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found"))
        );
    }
}
