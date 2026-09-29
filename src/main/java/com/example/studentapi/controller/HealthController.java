package com.example.studentapi.controller;

import com.example.studentapi.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public  String health(){
        return "stuent-api is running";
    }

}
