package com.example.studentapi.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Student {

    private Long id;
    @NotBlank(message = "学生姓名不能为空")
    private String name;

    @DecimalMin(value = "0.0",message = "成绩不能低于0")
    @DecimalMax(value ="100",message = "成绩不能大于100")
    private double score;

    public Student(Long id,String name,double score){
        this.id=id;
        this.name=name;
        this.score=score;
    }

    public Student(){

    }

    public Long getId(){
        return  id;
    }

    public String getName(){
        return  name;
    }

    public double getScore(){
        return score;
    }

    public void setId(Long id){
        this.id=id;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setScore(double score){
        this.score=score;
    }

}
