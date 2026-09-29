package com.example.studentapi.service;

import com.example.studentapi.mapper.StudentMapper;
import com.example.studentapi.model.PageResult;
import com.example.studentapi.model.Student;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;


import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentService {
    private final StudentMapper studentMapper;
    private final StringRedisTemplate stringRedisTemplate;//读写字符串
    private final ObjectMapper objectMapper;//student对象和json字符串的互相转换
    private static  final Logger log= LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentMapper studentMapper,
                          StringRedisTemplate stringRedisTemplate,
                          ObjectMapper objectMapper)
    {
        this.studentMapper = studentMapper;
        this.stringRedisTemplate=stringRedisTemplate;
        this.objectMapper=objectMapper;
    }

    //查询全部学生
    public List<Student> getAllStudents(){
        return studentMapper.findAllStudent();
    }

    //添加学生
    public Student addStudent(Student student){
        studentMapper.addStudent(student);
        return student;
    }

    //根据ID查询学生
    public Student getStudentById(Long id){
        log.info("开始查询学生，id={}", id);
        String key="student:" +id;

        String studentJson=stringRedisTemplate.opsForValue().get(key);

        //避免缓存穿透
        if ("_NULL_".equals(studentJson)){
            log.debug("学生空值缓存命中, id={}",id);
            return null;
        }

        try {
            //JSON转换成Student对象
            if (studentJson !=null){
                log.debug("学生缓存命中，id={}",id);
                return objectMapper.readValue(studentJson,Student.class);
            }

            Student student=studentMapper.findById(id);

            //Student对象转换成JSON
            if (student !=null){
                String json=objectMapper.writeValueAsString(student);

                stringRedisTemplate.opsForValue().set(key,json,10, TimeUnit.MINUTES);
            }else {
                log.warn("学生不存在,id={}",id);
                stringRedisTemplate.opsForValue().set(key,"_NULL_",2,TimeUnit.MINUTES);
            }

            return student;
        }catch (JacksonException exception){
            log.error("学生缓存数据转换失败,id={}",id,exception);
            throw new RuntimeException("学生缓存数据转换失败",exception);
        }
    }

    //根据ID修改学生信息
    public Student updateStudent(Long id,Student newStudent){
        String key="student:" +id;
        newStudent.setId(id);
        int rows=studentMapper.updateById(newStudent);
        if (rows==0){
            return null;
        }
        stringRedisTemplate.delete(key);
        return newStudent;
    }

    //根据ID删除学生
    public boolean deleteStudent(Long id){
        String key="student:" +id;
        int rows=studentMapper.deleteById(id);

        if (rows==0){
            return false;
        }

        stringRedisTemplate.delete(key);
        return true;
    }

    //查询及格的学生
    public List<Student> getPassedStudents(){
        return studentMapper.getPassedStudent();
    }

    //动态查询
    public PageResult searchStudents(String name,Double minScore,Double maxScore,int page,int pageSize){
        int offset=(page-1)*pageSize;
        List<Student> records=studentMapper.search(name, minScore, maxScore, offset, pageSize);

        Long total=studentMapper.count(name, minScore, maxScore);

        return new PageResult(records,total,page,pageSize);

    }

    //事务:添加两名学生
    @Transactional
    public List<Student> addTwoStudents(List<Student> students){
        studentMapper.addStudent(students.get(0));
        studentMapper.addStudent(students.get(1));
        return students;
    }
}
