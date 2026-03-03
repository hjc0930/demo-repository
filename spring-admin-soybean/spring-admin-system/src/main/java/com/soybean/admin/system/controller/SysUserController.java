package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysUser;
import com.soybean.admin.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/page")
    @RequirePermission("system:user:list")
    public Result<IPage<SysUser>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SysUser query) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        return userService.selectUserPage(page, query);
    }

    /**
     * 根据用户ID查询用户
     */
    @GetMapping("/{userId}")
    @RequirePermission("system:user:query")
    public Result<SysUser> getUser(@PathVariable Long userId) {
        SysUser user = userService.selectUserById(userId);
        return Result.ok(user);
    }

    /**
     * 新增用户
     */
    @PostMapping
    @RequirePermission("system:user:add")
    public Result<Void> add(@RequestBody SysUser user) {
        userService.insertUser(user);
        return Result.ok();
    }

    /**
     * 修改用户
     */
    @PutMapping
    @RequirePermission("system:user:edit")
    public Result<Void> edit(@RequestBody SysUser user) {
        userService.updateUser(user);
        return Result.ok();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    @RequirePermission("system:user:remove")
    public Result<Void> remove(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return Result.ok();
    }

    /**
     * 重置密码
     */
    @PutMapping("/reset-password")
    @RequirePermission("system:user:resetPwd")
    public Result<Void> resetPassword(@RequestParam Long userId, @RequestParam String password) {
        userService.resetPassword(userId, password);
        return Result.ok();
    }

    /**
     * 修改用户状态
     */
    @PutMapping("/status")
    @RequirePermission("system:user:edit")
    public Result<Void> changeStatus(@RequestParam Long userId, @RequestParam String status) {
        userService.updateUserStatus(userId, status);
        return Result.ok();
    }
}
