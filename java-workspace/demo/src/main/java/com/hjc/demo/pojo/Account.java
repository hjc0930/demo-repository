package com.hjc.demo.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Account {
    private Integer id;
    private String account;
}
