package com.hjc.demo.controllers;

import com.hjc.demo.dto.UserFindPageDto;
import com.hjc.demo.services.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping
    public List<UserFindPageDto> findAll() {
        return userService.findAll();
    }
}
