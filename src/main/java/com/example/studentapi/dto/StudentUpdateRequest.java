package com.example.studentapi.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudentUpdateRequest {
    @NotBlank(message = "学生姓名不能为空")
    private String name;

    @NotNull(message = "成绩不能为空")
    @DecimalMin(value = "0.0",message = "成绩不能小于0")
    @DecimalMax(value = "100.0",message = "成绩不能大于100")
    private Double score;

    public StudentUpdateRequest(){
    }

    public String getName(){
        return name;
    }

    public Double getScore(){
        return  score;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
