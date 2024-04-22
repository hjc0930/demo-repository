package com.hjc.demo.controllers;

import com.hjc.demo.dto.PaginationResult;
import com.hjc.demo.pojo.Student;
import com.hjc.demo.services.impl.StudentServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    @Resource
    private StudentServiceImpl studentServiceImpl;

    @GetMapping
    public PaginationResult<Student> findAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        return  studentServiceImpl.findAll(page, pageSize);
    }
}
