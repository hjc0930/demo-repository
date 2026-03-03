package com.hjc.blog.service;

import com.hjc.blog.dto.LoginDto;
import com.hjc.blog.dto.RegisterDto;
import com.hjc.blog.vo.LoginVo;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @param ip      客户端IP
     * @return 登录响应（包含Token）
     */
    LoginVo login(LoginDto request, String ip);

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 登录响应（注册成功后自动登录）
     */
    LoginVo register(RegisterDto request);
}
