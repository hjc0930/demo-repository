package com.blog.vo;

import com.blog.entitys.User;
import lombok.Data;

import java.util.List;

@Data
public class UserVo {
    private List<User> list;
    private int total;
}
