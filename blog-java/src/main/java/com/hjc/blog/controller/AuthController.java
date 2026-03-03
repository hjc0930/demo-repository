package com.hjc.blog.controller;

import com.hjc.blog.common.result.Result;
import com.hjc.blog.dto.LoginDto;
import com.hjc.blog.dto.RegisterDto;
import com.hjc.blog.service.AuthService;
import com.hjc.blog.vo.LoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理", description = "登录、注册等相关接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "支持用户名或邮箱登录")
    @PostMapping("/login")
    public Result<LoginVo> login(@Valid @RequestBody LoginDto request, HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        LoginVo response = authService.login(request, ip);
        return Result.success(response);
    }

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "注册成功后自动登录")
    @PostMapping("/register")
    public Result<LoginVo> register(@Valid @RequestBody RegisterDto request) {
        LoginVo response = authService.register(request);
        return Result.success(response);
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多级代理的情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
