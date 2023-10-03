package com.blog.controllers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.common.Result;
import com.blog.entitys.User;
import com.blog.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户列表")
    @GetMapping
    public List<User> getUserList() {
        return userService.list();
    }

    @ApiOperation(value = "分页获取用户列表")
    @GetMapping("/page")
    public Result getUserListPage(
            @ApiParam("页码")
            @RequestParam Integer pageNumber,
            @ApiParam("每页条数")
            @RequestParam Integer pageSize
    ) throws InterruptedException {
        Thread.sleep(600);
        Map<String, Object> userListPage = userService.getUserListPage(pageNumber, pageSize);
        return Result.success(userListPage);
    }

    @ApiOperation(value = "保存/更新用户", code = 200)
    @PostMapping
    public boolean saveUser(
            @ApiParam("用户")
            @RequestBody() User user) {
        return userService.saveUser(user);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public boolean deleteUser(
            @ApiParam("用户ID")
            @PathVariable Integer id){
        return userService.removeById(id);
    }
}
