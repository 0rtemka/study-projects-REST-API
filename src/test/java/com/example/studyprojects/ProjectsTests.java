package com.example.studyprojects;

import com.example.studyprojects.dto.ProjectDto;
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
@Sql(value = "/projects_test_before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/projects_test_after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProjectsTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private final String usernameIS = "stud00002608@study.ru";
    private final String usernameISAS = "stud00001901@study.ru";
    private final String usernameCS = "stud00002202@study.ru";
    private final String password = "12345";

    @Test
    public void takeProjectTest_ValidThemeId_OK() throws Exception {
        this.mockMvc.perform(post("http://localhost:8080/api/projects/1")
                        .with(httpBasic(usernameIS, password)))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("http://localhost:8080/api/students/1/projects")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].project_id", is(1)))
                .andExpect(jsonPath("$[0].topic", is("Java NIO")))
                .andExpect(jsonPath("$[0].group_id", is("IS")));

        this.mockMvc.perform(post("http://localhost:8080/api/projects/2")
                        .with(httpBasic(usernameISAS, password)))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("http://localhost:8080/api/students/2/projects")
                        .with(httpBasic(usernameISAS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].project_id", is(2)))
                .andExpect(jsonPath("$[0].topic", is("Python Multithreading")))
                .andExpect(jsonPath("$[0].group_id", is("ISAS")));

        this.mockMvc.perform(post("http://localhost:8080/api/projects/3")
                        .with(httpBasic(usernameCS, password)))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("http://localhost:8080/api/students/3/projects")
                        .with(httpBasic(usernameCS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].project_id", is(3)))
                .andExpect(jsonPath("$[0].topic", is("Kotlin Coroutines")))
                .andExpect(jsonPath("$[0].group_id", is("CS")));
    }

    @Test
    public void takeProjectTest_InvalidThemeId_BAD_REQUEST() throws Exception {
        this.mockMvc.perform(post("http://localhost:8080/api/projects/11")
                    .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project theme with id = 11 not found or unavailable")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/projects/11")));
    }

    @Test
    public void takeProjectTest_InvalidGroup_BAD_REQUEST() throws Exception {
        this.mockMvc.perform(post("http://localhost:8080/api/projects/2")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Student from IS can not take project from ISAS group")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/projects/2")));
    }

    @Test
    public void takeProjectTest_TooLargeGroup_BAD_REQUEST() throws Exception {
        init10Projects();

        String usernameIS1 = "stud00002003@study.ru";
        this.mockMvc.perform(post("http://localhost:8080/api/projects/1")
                    .with(httpBasic(usernameIS1, password)))
                .andExpect(status().isOk());

        String usernameIS2 = "stud00002004@study.ru";
        this.mockMvc.perform(post("http://localhost:8080/api/projects/1")
                        .with(httpBasic(usernameIS2, password)))
                .andExpect(status().isOk());

        String usernameIS3 = "stud00002005@study.ru";
        this.mockMvc.perform(post("http://localhost:8080/api/projects/1")
                        .with(httpBasic(usernameIS3, password)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project Java NIO has maximum size of group (3 people)")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/projects/1")));
    }

    @Test
    public void getProjectByIdTest_ValidId_OK() throws Exception {
        this.mockMvc.perform(post("http://localhost:8080/api/projects/1")
                        .with(httpBasic(usernameIS, password)))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("http://localhost:8080/api/projects/1")
                .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.project_id", is(1)))
                .andExpect(jsonPath("$.topic", is("Java NIO")))
                .andExpect(jsonPath("$.group_id", is("IS")));
    }

    @Test
    public void getProjectByIdTest_InvalidId_BAD_REQUEST() throws Exception {
        this.mockMvc.perform(post("http://localhost:8080/api/projects/1")
                        .with(httpBasic(usernameIS, password)))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("http://localhost:8080/api/projects/2")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project with id = 2 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/projects/2")));
    }

    @Test
    public void getAllProjectsTest_Pagination_OK() throws Exception {
        init10Projects();

        this.mockMvc.perform(get("http://localhost:8080/api/projects")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?page=0&size=3")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].topic", is("Java NIO")))
                .andExpect(jsonPath("$[0].group_id", is("IS")))
                .andExpect(jsonPath("$[1].topic", is("Python Multithreading")))
                .andExpect(jsonPath("$[1].group_id", is("ISAS")))
                .andExpect(jsonPath("$[2].topic", is("Kotlin Coroutines")))
                .andExpect(jsonPath("$[2].group_id", is("CS")));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?page=1&size=3")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].topic", is("Java Collections Framework")))
                .andExpect(jsonPath("$[0].group_id", is("IS")))
                .andExpect(jsonPath("$[1].topic", is("Java Stream API")))
                .andExpect(jsonPath("$[1].group_id", is("ISAS")))
                .andExpect(jsonPath("$[2].topic", is("Python JSON")))
                .andExpect(jsonPath("$[2].group_id", is("CS")));
    }

    @Test
    public void getAllProjectsTest_Filtering1_OK() throws Exception {
        init10Projects();

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=active")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(10)));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=inactive")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=marked")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=unmarked")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    public void getAllProjectsTest_Filtering2_OK() throws Exception {
        init10Projects();
        rateProjects();

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=active")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(10)));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=inactive")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=marked")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(5)));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=marked&size=2")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].topic", is("Java NIO")))
                .andExpect(jsonPath("$[0].group_id", is("IS")))
                .andExpect(jsonPath("$[1].topic", is("Python Multithreading")))
                .andExpect(jsonPath("$[1].group_id", is("ISAS")));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=unmarked")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(5)));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?status=unmarked&size=3")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].topic", is("Python JSON")))
                .andExpect(jsonPath("$[0].group_id", is("CS")))
                .andExpect(jsonPath("$[1].topic", is("Java JSON")))
                .andExpect(jsonPath("$[1].group_id", is("IS")))
                .andExpect(jsonPath("$[2].topic", is("Kotlin JSON")))
                .andExpect(jsonPath("$[2].group_id", is("ISAS")));
    }

    @Test
    public void getAllProjects_Filtering3_OK() throws Exception {
        init10Projects();

        this.mockMvc.perform(get("http://localhost:8080/api/projects?group=is")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].topic", is("Java NIO")))
                .andExpect(jsonPath("$[1].topic", is("Java Collections Framework")))
                .andExpect(jsonPath("$[2].topic", is("Java JSON")))
                .andExpect(jsonPath("$[3].topic", is("Python Django")));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?group=isas")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].topic", is("Python Multithreading")))
                .andExpect(jsonPath("$[1].topic", is("Java Stream API")))
                .andExpect(jsonPath("$[2].topic", is("Kotlin JSON")));

        this.mockMvc.perform(get("http://localhost:8080/api/projects?group=cs")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].topic", is("Kotlin Coroutines")))
                .andExpect(jsonPath("$[1].topic", is("Python JSON")))
                .andExpect(jsonPath("$[2].topic", is("Java Spring Framework")));
    }

    @Test
    public void getAllProjectsTest_Filtering4_BAD_REQUEST() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/api/projects?group=css")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Group with name 'CSS' not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/projects")));
    }

    @Test
    public void rateProjectTest_BaseLogic_OK() throws Exception {
        init10Projects();
        ProjectDto projectDto = ProjectDto.builder()
                .mark(10)
                .build();

        this.mockMvc.perform(put("http://localhost:8080/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(projectDto))
                    .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("http://localhost:8080/api/themes/1")
                    .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available", is(false)));

        this.mockMvc.perform(post("http://localhost:8080/api/projects/1")
                    .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project theme with id = 1 not found or unavailable")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/projects/1")));
    }

    @Test
    public void rateProjectTest_InvalidId_BAD_REQUEST() throws Exception {
        ProjectDto projectDto = ProjectDto.builder()
                .mark(10)
                .build();

        this.mockMvc.perform(put("http://localhost:8080/api/projects/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(projectDto))
                    .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project with id = 1 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/projects/1")));
    }

    @Test
    public void rateProjectTest_InvalidMark_BAD_REQUEST() throws Exception {
        ProjectDto projectDto = ProjectDto.builder()
                .mark(16)
                .build();

        this.mockMvc.perform(put("http://localhost:8080/api/projects/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(projectDto))
                    .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.mark", is("Mark cannot be greater than 15")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));

        projectDto.setMark(0);

        this.mockMvc.perform(put("http://localhost:8080/api/projects/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(projectDto))
                    .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.mark", is("Mark cannot be less than 1")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));
    }

    @Test
    public void deleteProjectTest() throws Exception {
        init10Projects();

        this.mockMvc.perform(delete("http://localhost:8080/api/projects/1")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(delete("http://localhost:8080/api/projects/1")
                        .with(httpBasic(usernameIS, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Project with id 1 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/projects/1")));
    }

    private void init10Projects() throws Exception {
        this.mockMvc.perform(post("http://localhost:8080/api/projects/1")
                        .with(httpBasic(usernameIS, password)));
        this.mockMvc.perform(post("http://localhost:8080/api/projects/2")
                        .with(httpBasic(usernameISAS, password)));
        this.mockMvc.perform(post("http://localhost:8080/api/projects/3")
                        .with(httpBasic(usernameCS, password)));
        this.mockMvc.perform(post("http://localhost:8080/api/projects/4")
                        .with(httpBasic(usernameIS, password)));
        this.mockMvc.perform(post("http://localhost:8080/api/projects/5")
                        .with(httpBasic(usernameISAS, password)));
        this.mockMvc.perform(post("http://localhost:8080/api/projects/6")
                        .with(httpBasic(usernameCS, password)));
        this.mockMvc.perform(post("http://localhost:8080/api/projects/7")
                        .with(httpBasic(usernameIS, password)));
        this.mockMvc.perform(post("http://localhost:8080/api/projects/8")
                        .with(httpBasic(usernameISAS, password)));
        this.mockMvc.perform(post("http://localhost:8080/api/projects/9")
                        .with(httpBasic(usernameCS, password)));
        this.mockMvc.perform(post("http://localhost:8080/api/projects/10")
                        .with(httpBasic(usernameIS, password)));
    }

    private void rateProjects() throws Exception {
        ProjectDto projectDto = ProjectDto.builder()
                .mark(10)
                .build();

        this.mockMvc.perform(put("http://localhost:8080/api/projects/1")
                .with(httpBasic(usernameIS, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(projectDto)));
        this.mockMvc.perform(put("http://localhost:8080/api/projects/2")
                .with(httpBasic(usernameIS, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(projectDto)));
        this.mockMvc.perform(put("http://localhost:8080/api/projects/3")
                .with(httpBasic(usernameIS, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(projectDto)));
        this.mockMvc.perform(put("http://localhost:8080/api/projects/4")
                .with(httpBasic(usernameIS, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(projectDto)));
        this.mockMvc.perform(put("http://localhost:8080/api/projects/5")
                .with(httpBasic(usernameIS, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(projectDto)));
    }
}

