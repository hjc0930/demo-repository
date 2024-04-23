package com.hjc.demo.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hjc.demo.mappers.StudentMapper;
import com.hjc.demo.dto.PaginationResult;
import com.hjc.demo.pojo.Student;
import com.hjc.demo.services.StudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentMapper studentMapper;

    @Override
    public PaginationResult<Student> findAll(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Student> studentList = studentMapper.findAll();
        PageInfo<Student> studentPageInfo = new PageInfo<>(studentList);
        Optional<PageInfo<Student>> studentOptional = Optional.ofNullable(studentPageInfo);

        return studentOptional
                .map(item -> PaginationResult
                .<Student>builder()
                .page(item.getPageNum())
                .pageSize(item.getPageSize())
                .total(item.getTotal())
                .list(item.getList())
                .build()
        ).orElse(null);
    }
}
