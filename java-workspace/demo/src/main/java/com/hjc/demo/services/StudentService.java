package com.hjc.demo.services;

import com.hjc.demo.dto.PaginationResult;
import com.hjc.demo.pojo.Student;


public interface StudentService {
    PaginationResult<Student> findAll(Integer page, Integer pageSize);
}
