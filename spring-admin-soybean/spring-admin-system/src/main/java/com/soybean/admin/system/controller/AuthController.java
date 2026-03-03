package com.soybean.admin.system.controller;

import com.soybean.admin.common.response.Result;
import com.soybean.admin.system.dto.LoginDto;
import com.soybean.admin.system.dto.LoginResultDto;
import com.soybean.admin.system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<LoginResultDto> login(@Valid @RequestBody LoginDto loginDto) {
        LoginResultDto result = authService.login(loginDto);
        return Result.ok(result);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");
        authService.logout(token);
        return Result.ok();
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh-token")
    public Result<String> refreshToken(@RequestParam String refreshToken) {
        String newAccessToken = authService.refreshToken(refreshToken);
        return Result.ok(newAccessToken);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/user-info")
    public Result<LoginResultDto.UserInfo> getUserInfo() {
        LoginResultDto.UserInfo userInfo = authService.getCurrentUserInfo();
        return Result.ok(userInfo);
    }
}
