package com.example.studentapi.controller;

import com.example.studentapi.model.Student;
import com.example.studentapi.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    void shouldReturnStudentWhenStudentExists() throws Exception {
        // 1. 准备一个ID为1、姓名为“银月”、成绩为96的Student对象
        Student student=new Student(1L,"银月",96);

        // 2. 规定调用studentService.getStudentById(1L)时返回该对象
        given(studentService.getStudentById(1L)).willReturn(student);

        // 3. 使用mockMvc发送GET /students/1
        mockMvc.perform(get("/students/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        mockMvc.perform(get("/students/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("查询成功"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("银月"))
                .andExpect(jsonPath("$.data.score").value(96.0));

    }

    @Test
    void shouldReturn404WhenStudentDoesNotExist() throws Exception{
        given(studentService.getStudentById(999L)).willReturn(null);

        mockMvc.perform(get("/students/{id}",999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("学生不存在"));
    }

    @Test
    void shouldReturn400WhenStudentRequestIsInvalid() throws Exception{
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "name":"",
                        "score":101
                        }
                        """))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("参数校验失败"))
                .andExpect(jsonPath("$.data.name").value("学生姓名不能为空"))
                .andExpect(jsonPath("$.data.score").value("成绩不能大于100"));
    }
}