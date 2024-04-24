package com.hjc.demo.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class User {
    private Integer id;
    private String username;
    private String password;
    private Date createTime;
    private Date updateTime;
}
