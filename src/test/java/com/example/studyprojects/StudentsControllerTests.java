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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/create_students.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/drop_students.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class StudentsControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    public void findAllStudentsTest() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/api/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student_id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[1].student_id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Bob")))
                .andExpect(jsonPath("$[2].student_id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Sam")));
    }

    @Test
    public void findStudentByIdTest_ValidId_OK() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/api/students/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student_id", is(2)))
                .andExpect(jsonPath("$.name", is("Bob")))
                .andExpect(jsonPath("$.email", is("stud00001009@study.ru")))
                .andExpect(jsonPath("$.mark", is(61)))
                .andExpect(jsonPath("$.group", is("ISAS")));
    }

    @Test
    public void findStudentByIdTest_InvalidId_NOT_FOUND() throws Exception {
        this.mockMvc.perform(get("http://localhost:8080/api/students/4"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Student with id = 4 not found")))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.path", is("/api/students/4")));
    }

    @Test
    public void createStudentTest_ValidData_CREATED() throws Exception {
        Student stud =  Student.builder()
                .name("Mary").email("stud00002808@study.ru").mark(98).group(Group.IS).build();

        this.mockMvc.perform(post("http://localhost8080/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andExpect(status().isCreated());

        this.mockMvc.perform(get("http://localhost:8080/api/students/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Mary")))
                .andExpect(jsonPath("$.email", is("stud00002808@study.ru")))
                .andExpect(jsonPath("$.mark", is(98)))
                .andExpect(jsonPath("$.group", is("IS")));
    }

    @Test
    public void createStudentTest_InvalidNameAndEmail1_BAD_REQUEST() throws Exception {
        Student stud =  Student.builder()
                .name("").email("stud000028@study.ru").mark(0).group(Group.IS).build();

        this.mockMvc.perform(post("http://localhost:8080/api/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name", is("Name should not be empty")))
                .andExpect(jsonPath("$.errors.email", is("Invalid email. Format = 'studXXXXXXXX@study.ru'")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/students")));
    }

    @Test
    public void createStudentTest_InvalidNameAndEmail2_BAD_REQUEST() throws Exception {
        Student stud =  Student.builder()
                .name("").email("").mark(100).group(Group.IS).build();

        this.mockMvc.perform(post("http://localhost:8080/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name", is("Name should not be empty")))
                .andExpect(jsonPath("$.errors.email", is("Invalid email. Format = 'studXXXXXXXX@study.ru'")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/students")));
    }

    @Test
    public void createStudentTest_InvalidMark1_BAD_REQUEST() throws Exception {
        Student stud =  Student.builder()
                .name("Mary").email("stud00002808@study.ru").mark(-1).group(Group.IS).build();

        this.mockMvc.perform(post("http://localhost:8080/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.mark", is("Mark can not be below 0")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/students")));
    }

    @Test
    public void createStudentTest_InvalidMark2_BAD_REQUEST() throws Exception {
        Student stud =  Student.builder()
                .name("Mary").email("stud00002808@study.ru").mark(101).group(Group.IS).build();

        this.mockMvc.perform(post("http://localhost:8080/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.mark", is("Mark can not be greater than 100")))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.path", is("/api/students")));
    }

    @Test
    public void editStudentTest_ValidId_OK() throws Exception {
        Student stud = Student.builder()
                .name("Elizabeth").email("stud00002907@study.ru").mark(67).group(Group.ISAS).build();

        this.mockMvc.perform(put("http://localhost:8080/api/students/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(get("http://localhost:8080/api/students/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Elizabeth")))
                .andExpect(jsonPath("$.email", is("stud00002907@study.ru")))
                .andExpect(jsonPath("$.mark", is(67)))
                .andExpect(jsonPath("$.group", is("ISAS")));
    }

    @Test
    public void editStudentTest_InvalidId_NOT_FOUND() throws Exception {
        Student stud = Student.builder()
                .name("Elizabeth").email("stud00002907@study.ru").mark(67).group(Group.ISAS).build();

        this.mockMvc.perform(put("http://localhost:8080/api/students/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(stud)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Student with id = 4 not found")))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.path", is("/api/students/4")));
    }
}
