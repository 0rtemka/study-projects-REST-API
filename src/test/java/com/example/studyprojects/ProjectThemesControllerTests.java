package com.example.studyprojects;

import com.example.studyprojects.dto.ProjectThemeDto;
import com.example.studyprojects.model.Group;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/themes-list-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/themes-list-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProjectThemesControllerTests {
    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    private final String username = "stud00002608@study.ru";
    private final String password = "password";

    @Test
    public void findAllThemesTest() throws Exception {
        this.mock.perform(get("http://localhost:8080/api/themes").with(
                    httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }

    @Test
    public void findThemeByIdTest_ValidId_OK() throws Exception {
        this.mock.perform(get("http://localhost:8080/api/themes/id/1").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic", is("Concurrency in Java")))
                .andExpect(jsonPath("$.group", is("IS")));
    }

    @Test
    public void findThemeByIdTest_InvalidId_BAD_REQUEST() throws Exception {
        this.mock.perform(get("http://localhost:8080/api/themes/id/7").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project theme with id = 7 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/themes/id/7")));
    }

    @Test
    public void findThemesByGroupTest_IS() throws Exception {
        this.mock.perform(get("http://localhost:8080/api/themes/is").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].topic", is("Concurrency in Java")))
                .andExpect(jsonPath("$[1].topic", is("Concurrency in Python")))
                .andExpect(jsonPath("$[2].topic", is("Concurrency in Kotlin")));
    }

    @Test
    public void findThemesByGroupTest_ISAS() throws Exception {
        this.mock.perform(get("http://localhost:8080/api/themes/isas").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].topic", is("Stream API in Java")))
                .andExpect(jsonPath("$[1].topic", is("LINQ in C#")));
    }

    @Test
    public void findThemesByGroupTest_CS() throws Exception {
        this.mock.perform(get("http://localhost:8080/api/themes/cs").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].topic", is("Collections framework in Java")));
    }

    @Test
    public void findThemesByGroupTest_InvalidGroup_BAD_REQUEST() throws Exception {
        this.mock.perform(get("http://localhost:8080/api/themes/css").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Group with name 'CSS' not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/themes/css")));
    }

    @Test
    public void addThemeTest_ValidData_OK() throws Exception {
        ProjectThemeDto projectTheme = ProjectThemeDto.builder()
                .topic("Java NIO")
                .group(Group.CS)
                .build();

        this.mock.perform(post("http://localhost:8080/api/themes").with(
                                httpBasic(username, password)
                        )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(projectTheme)))
                .andDo(print())
                .andExpect(status().isCreated());

        this.mock.perform(get("http://localhost:8080/api/themes/id/10").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic", is("Java NIO")))
                .andExpect(jsonPath("$.group", is("CS")));
    }

    @Test
    public void addThemeTest_InvalidTopic_BAD_REQUEST() throws Exception {
        ProjectThemeDto projectTheme = ProjectThemeDto.builder()
                .topic("")
                .group(Group.CS)
                .build();


        this.mock.perform(post("http://localhost:8080/api/themes").with(
                                httpBasic(username, password)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(projectTheme)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.topic", is("Topic should not be empty")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));
    }

    @Test
    public void addThemeTest_InvalidGroup_BAD_REQUEST() throws Exception {
        ProjectThemeDto projectTheme = ProjectThemeDto.builder()
                .topic("Java NIO")
                .build();

        this.mock.perform(post("http://localhost:8080/api/themes").with(
                                httpBasic(username, password)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(projectTheme)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.group", is("Group can not be null")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));
    }

    @Test
    public void editThemeTest_ValidData_OK() throws Exception {
        ProjectThemeDto projectTheme = ProjectThemeDto.builder()
                .topic("Concurrency in Golang")
                .group(Group.IS)
                .build();

        this.mock.perform(put("http://localhost:8080/api/themes/1").with(
                                httpBasic(username, password)
                        )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(projectTheme)))
                .andDo(print())
                .andExpect(status().isOk());

        this.mock.perform(get("http://localhost:8080/api/themes/id/1").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic", is("Concurrency in Golang")))
                .andExpect(jsonPath("$.group", is("IS")));
    }

    @Test
    public void editThemeTest_InvalidId_BAD_REQUEST() throws Exception {
        ProjectThemeDto projectTheme = ProjectThemeDto.builder()
                .topic("Concurrency in Golang")
                .group(Group.IS)
                .build();

        this.mock.perform(put("http://localhost:8080/api/themes/7").with(
                                httpBasic(username, password)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(projectTheme)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project theme with id = 7 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/themes/7")));

    }

    @Test
    public void editThemeTest_InvalidData_BAD_REQUEST() throws Exception {
        ProjectThemeDto projectTheme = ProjectThemeDto.builder()
                .topic("")
                .build();

        this.mock.perform(put("http://localhost:8080/api/themes/1").with(
                                httpBasic(username, password)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(projectTheme)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.topic", is("Topic should not be empty")))
                .andExpect(jsonPath("$.errors.group", is("Group can not be null")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));
    }

    @Test
    public void deleteThemeTest_ValidId_OK() throws Exception {
        this.mock.perform(delete("http://localhost:8080/api/themes/1").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isOk());

        this.mock.perform(get("http://localhost:8080/api/themes/id/1").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project theme with id = 1 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/themes/id/1")));
    }

    @Test
    public void deleteThemeTest_InvalidId_BAD_REQUEST() throws Exception {
        this.mock.perform(delete("http://localhost:8080/api/themes/7").with(
                        httpBasic(username, password)
                ))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project theme with id = 7 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/themes/7")));
    }

}
