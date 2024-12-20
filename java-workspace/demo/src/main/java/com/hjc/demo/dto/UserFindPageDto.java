package com.hjc.demo.dto;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Component
public class UserFindPageDto {
    private Integer id;
    private String username;
    private String password;
    private String createTime;
    private String updateTime;
}
