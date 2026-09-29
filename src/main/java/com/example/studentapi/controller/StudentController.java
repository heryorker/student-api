package com.example.studentapi.controller;


import com.example.studentapi.common.ApiResponse;
import com.example.studentapi.dto.StudentUpdateRequest;
import com.example.studentapi.model.PageResult;
import com.example.studentapi.model.Student;
import com.example.studentapi.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudent() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable
                                                  @Min(value = 1, message = "ID必须大于0")Long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            ApiResponse<Student> response=new ApiResponse<>(404,"学生不存在",null);
            return ResponseEntity.status(404).body(response);
        }

        ApiResponse<Student> response=new ApiResponse<>(200,"查询成功",student);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public Student addStudent(@Valid @RequestBody Student student) {
        studentService.addStudent(student);
        return student;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable
                                                 @Min(value = 1, message = "ID必须大于0")Long id,
                                                 @Valid @RequestBody StudentUpdateRequest request) {
        Student newStudent=new Student();
        newStudent.setName(request.getName());
        newStudent.setScore(request.getScore());

        Student student = studentService.updateStudent(id, newStudent);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable
                                              @Min(value = 1, message = "ID必须大于0")Long id) {
        boolean deleted=studentService.deleteStudent(id);
        if (deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/passed")
    public List<Student> getPassedStudent(){
        return studentService.getPassedStudents();
    }

    @GetMapping("/search")
    public PageResult searchStudents(
            @RequestParam(required = false)String name,
            @RequestParam(required = false)Double minScore,
            @RequestParam(required = false)Double maxScore,
            @RequestParam(defaultValue = "1")
            @Min(value = 1,message = "页码不能小于1")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1,message = "每页数量不能小于1")
            @Max(value = 100,message = "每页数量不能超过100")
            int pageSize
    ){
        return studentService.searchStudents(name, minScore, maxScore,page,pageSize);
    }

    @PostMapping("/bath")
    public ResponseEntity<?> addTwoStudents( @RequestBody
                                             @NotEmpty(message = "学生列表不能为空")
                                                 List<@Valid Student> students){

        if (students.size()!=2){
            Map<String,String> error=new HashMap<>();
            error.put("message","必须提交两名学生");
            return ResponseEntity.badRequest().body(error);
        }

        List<Student> result=studentService.addTwoStudents(students);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }
}

