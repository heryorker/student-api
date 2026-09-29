package com.example.studentapi.mapper;

import com.example.studentapi.model.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    @Insert("""
            INSERT INTO student(id,name,score)
            VALUES(#{id},#{name},#{score})
            """)
    int addStudent(Student student);

    @Select("SELECT * FROM student")
    List<Student> findAllStudent();

    @Select("""
            SELECT * FROM student
            WHERE id=#{id}
            """)
    Student findById(long id);

    @Update("""
            UPDATE student
            SET score=#{score},name=#{name}
            WHERE id=#{id}
            """)
    int updateById(Student student);

    @Delete("""
            DELETE FROM student
            WHERE id=#{id}
            """)
    int deleteById(long id);

    @Select("""
            SELECT * FROM student
            WHERE score>=60
            """)
    List<Student> getPassedStudent();


    @Select("""
            <script>
            SELECT * FROM student
            <where>
            <if test="name != null and name!=''"> name LIKE CONCAT('%',#{name},'%')
            </if>
            
            <if test="minScore != null"> AND score>=#{minScore}
            </if>
            
            <if test="maxScore != null"> AND score&lt;=#{maxScore}
            </if>
            </where>
            ORDER BY id
            LIMIT #{offset},#{pageSize}
            </script>
            """)
    List<Student> search(
            @Param("name")String name,
            @Param("minScore")Double minScore,
            @Param("maxScore")Double maxScore,
            @Param("offset")int offset,
            @Param("pageSize")int pageSize
    );

    @Select("""
            <script>
            SELECT COUNT(*) FROM student
            <where>
            <if test="name != null and name!=''"> name LIKE CONCAT('%',#{name},'%')
            </if>
            
            <if test="minScore != null"> AND score>=#{minScore}
            </if>
            
            <if test="maxScore != null"> AND score&lt;=#{maxScore}
            </if>
            </where>
            </script>
            """)
    long count(
            @Param("name")String name,
            @Param("minScore")Double minScore,
            @Param("maxScore")Double maxScore
    );


}
