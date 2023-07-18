package com.example.studyprojects;

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

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/students-list-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/students-list-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class StudentsControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    private final String username = "stud00002608@study.ru";
    private final String password = "password";

    @Test
    public void findAllStudentsTest() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/api/students")
                        .with(httpBasic(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student_id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Artem")))
                .andExpect(jsonPath("$[0].group", is("IS")))
                .andExpect(jsonPath("$[0].role", is("ADMIN")))
                .andExpect(jsonPath("$[1].student_id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Kirill")))
                .andExpect(jsonPath("$[1].role", is("USER")))
                .andExpect(jsonPath("$[1].group", is("CS")))
                .andExpect(jsonPath("$[2].student_id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Sergey")))
                .andExpect(jsonPath("$[2].group", is("ISAS")))
                .andExpect(jsonPath("$[2].role", is("USER")));
    }

    @Test
    public void findStudentByIdTest_ValidId_OK() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/api/students/2").with(httpBasic(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student_id", is(2)))
                .andExpect(jsonPath("$.name", is("Kirill")))
                .andExpect(jsonPath("$.email", is("stud00002206@study.ru")))
                .andExpect(jsonPath("$.mark", is(76)))
                .andExpect(jsonPath("$.group", is("CS")));
    }

    @Test
    public void findStudentByIdTest_InvalidId_BAD_REQUEST() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/api/students/4").with(httpBasic(username, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Student with id = 4 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/students/4")));
    }

    @Test
    public void editStudentTest_ValidId_OK() throws Exception {
        Student stud = Student.builder()
                .name("Elizabeth").email("stud00002907@study.ru").mark(67).group(Group.ISAS).build();

        this.mockMvc.perform(put("http://localhost:8080/api/students/3").with(httpBasic(username, password))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("http://localhost:8080/api/students/3").with(httpBasic(username, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Elizabeth")))
                .andExpect(jsonPath("$.email", is("stud00002907@study.ru")))
                .andExpect(jsonPath("$.mark", is(67)))
                .andExpect(jsonPath("$.group", is("ISAS")));
    }

    @Test
    public void editStudentTest_InvalidId_BAD_REQUEST() throws Exception {
        Student stud = Student.builder()
                .name("Elizabeth").email("stud00002907@study.ru").mark(67).group(Group.ISAS).build();

        this.mockMvc.perform(put("http://localhost:8080/api/students/4").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Student with id = 4 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/students/4")));
    }

    @Test
    public void editStudentTest_InvalidNameAndEmail1_BAD_REQUEST() throws Exception {
        Student stud = Student.builder()
                .name("").email("stud000029@study.ru").mark(67).group(Group.ISAS).build();

        this.mockMvc.perform(put("http://localhost:8080/api/students/3").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name", is("Name should not be empty")))
                .andExpect(jsonPath("$.errors.email", is("Invalid email. Format = 'studXXXXXXXX@study.ru'")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));
    }

    @Test
    public void editStudentTest_InvalidNameAndEmail2_BAD_REQUEST() throws Exception {
        Student stud = Student.builder()
                .name("").email("").mark(67).group(Group.ISAS).build();

        this.mockMvc.perform(put("http://localhost:8080/api/students/3").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name", is("Name should not be empty")))
                .andExpect(jsonPath("$.errors.email", is("Invalid email. Format = 'studXXXXXXXX@study.ru'")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));
    }

    @Test
    public void editStudentTest_InvalidMark1_BAD_REQUEST() throws Exception {
        Student stud = Student.builder()
                .name("Elizabeth").email("stud00002907@study.ru").mark(-1).group(Group.ISAS).build();

        this.mockMvc.perform(put("http://localhost:8080/api/students/3").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.mark", is("Mark can not be below 0")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));
    }

    @Test
    public void editStudentTest_InvalidMark2_BAD_REQUEST() throws Exception {
        Student stud = Student.builder()
                .name("Elizabeth").email("stud00002907@study.ru").mark(101).group(Group.ISAS).build();

        this.mockMvc.perform(put("http://localhost:8080/api/students/3").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.mark", is("Mark can not be greater than 100")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")));
    }

    @Test
    public void deleteStudentTest_ValidId_OK() throws Exception {
        this.mockMvc.perform(delete("http://localhost:8080/api/students/2").with(httpBasic(username, password)))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("http://localhost:8080/api/students/2").with(httpBasic(username, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Student with id = 2 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/students/2")));
    }

    @Test
    public void deleteStudentTest_InvalidId_OK() throws Exception {
        this.mockMvc.perform(delete("http://localhost:8080/api/students/4").with(httpBasic(username, password)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Student with id = 4 not found")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/students/4")));
    }
}
