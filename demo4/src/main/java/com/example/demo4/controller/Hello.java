package com.example.demo4.controller;

import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@RestController
public class Hello {
    @RequestMapping("/")
    public String hello() {
        return "It's work!";
    }

    @PostMapping(value = "/body")
    public String body(@RequestBody String comments) {
        System.out.println(comments);
        return comments;
    }
}
