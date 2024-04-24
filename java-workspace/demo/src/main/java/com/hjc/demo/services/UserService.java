package com.hjc.demo.services;

import com.hjc.demo.dto.UserFindPageDto;

import java.util.List;

public interface UserService {
    List<UserFindPageDto> findAll();
}
