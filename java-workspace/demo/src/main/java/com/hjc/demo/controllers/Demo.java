package com.hjc.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {
    @RequestMapping("/ping")
    public String demo() {
        return "It's work!!!";
    }
}
