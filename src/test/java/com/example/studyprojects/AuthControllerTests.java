package com.example.studyprojects;

import com.example.studyprojects.dto.ProjectThemeDto;
import com.example.studyprojects.model.Group;
import com.example.studyprojects.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/auth_test_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/auth_test_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private final String username = "stud00002608@study.ru";
    private final String password = "password";

    @Test
    public void closedResourceGet() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/api/students"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(get("http://localhost:8080/api/themes"))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        this.mockMvc.perform(get("http://localhost:8080/api/themes/is"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void registerUser() throws Exception {
        String content = "{\n" +
                "    \"name\": \"Ivanov Sergey\",\n" +
                "    \"email\": \"stud00001507@study.ru\",\n" +
                "    \"password\": \"12345\",\n" +
                "    \"group\": \"CS\"\n" +
                "}";

        this.mockMvc.perform(post("http://localhost:8080/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("http://localhost:8080/api/students").with(
                    httpBasic("stud00001507@study.ru", "12345")
                ))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getResourceWithoutAuthority() throws Exception {
        ProjectThemeDto projectTheme = ProjectThemeDto.builder()
                .topic("Java NIO")
                .group(Group.CS)
                .build();

        Student student = Student.builder()
                .name("Elizabeth").email("stud00002907@study.ru").mark(67).group(Group.ISAS).build();


        this.mockMvc.perform(post("http://localhost:8080/api/themes").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(projectTheme)))
                .andExpect(status().isForbidden());

        this.mockMvc.perform(put("http://localhost:8080/api/themes/1").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(projectTheme)))
                .andExpect(status().isForbidden());

        this.mockMvc.perform(delete("http://localhost:8080/api/themes/1").with(httpBasic(username, password)))
                .andExpect(status().isForbidden());

        this.mockMvc.perform(put("http://localhost:8080/api/students/1").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(student)))
                .andExpect(status().isForbidden());

        this.mockMvc.perform(delete("http://localhost:8080/api/students/1").with(httpBasic(username, password)))
                .andExpect(status().isForbidden());
    }
}
