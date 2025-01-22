package com.hjc.demo.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {
    @RequestMapping("/ping")
    public String demo() {
        return "It's work!!!";
    }

    @RequestMapping(value = "/body", method = RequestMethod.PUT)
    public String body(@RequestBody String comments) {
        return "";
    }
}
