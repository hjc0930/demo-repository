package com.example.common.mappers;

import com.example.common.entitys.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> findAll();
}
