package com.hjc.demo.services.impl;

import com.hjc.demo.dto.UserFindPageDto;
import com.hjc.demo.mappers.UserMapper;
import com.hjc.demo.pojo.User;
import com.hjc.demo.services.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserFindPageDto> findAll() {
        List<User> userList = userMapper.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<UserFindPageDto> userFindPageDtos = userList.stream()
                .map(obj -> {
                    UserFindPageDto userFindPageDto = new UserFindPageDto();
                    BeanUtils.copyProperties(obj, userFindPageDto);
                    userFindPageDto.setCreateTime(obj.getCreateTime().toString());
                    userFindPageDto.setUpdateTime(obj.getUpdateTime().toString());
                    return userFindPageDto;
                })
                .toList();
        return userFindPageDtos;
    }
}
