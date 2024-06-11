package com.hjc.demo.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Student {
    private Integer id;
    private String name;
    private String gender;
    private Integer age;
    private String sClass;
    private Integer score;
//    private Account accountInfo;
}
