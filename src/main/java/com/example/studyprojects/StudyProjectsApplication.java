package com.example.studyprojects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class StudyProjectsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyProjectsApplication.class, args);
    }

}
