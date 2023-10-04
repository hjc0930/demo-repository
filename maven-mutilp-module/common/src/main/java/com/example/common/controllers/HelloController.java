package com.example.common.controllers;

import com.example.common.entitys.Student;
import com.example.common.mappers.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {
    @Autowired
    private StudentMapper studentMapper;

    @GetMapping
    public List<Student> findAll() {
        return studentMapper.findAll();
    }
}
