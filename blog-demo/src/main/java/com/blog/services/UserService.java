package com.blog.services;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entitys.User;
import com.blog.mappers.UserMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    public boolean saveUser(User user) {
        if(user.getId() == null) {
            return save(user);
        }
        return updateById(user);
    }

    public  Map<String, Object> getUserListPage(Integer pageNum, Integer pageSize) {
        IPage<User> pageParam = new Page<>(pageNum, pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();

        wrapper.orderByDesc("id");

        IPage<User> pageResult = page(pageParam, wrapper);

        Map<String, Object> map = new HashMap<>();

        map.put("list", pageResult.getRecords());
        map.put("total", pageResult.getTotal());

        return map;
    }
}
