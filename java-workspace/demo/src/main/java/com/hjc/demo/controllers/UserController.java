package com.hjc.demo.controllers;

import com.hjc.demo.dto.UserFindPageDto;
import com.hjc.demo.pojo.User;
import com.hjc.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping
    public List<UserFindPageDto> findAll() {
        return this.userService.findAll();
    }
}
