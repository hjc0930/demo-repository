package com.hjc.blog.service.impl;

import com.hjc.blog.common.exception.BusinessException;
import com.hjc.blog.common.result.ResultCodeEnum;
import com.hjc.blog.common.utils.JwtUtil;
import com.hjc.blog.dto.LoginDto;
import com.hjc.blog.dto.RegisterDto;
import com.hjc.blog.entity.User;
import com.hjc.blog.vo.LoginVo;
import com.hjc.blog.service.AuthService;
import com.hjc.blog.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public LoginVo login(LoginDto request, String ip) {
        String username = request.getUsername();

        // 查找用户（支持用户名或邮箱登录）
        User user = userService.getByUsername(username);
        if (user == null) {
            // 尝试使用邮箱登录
            user = userService.getByEmail(username);
        }

        // 验证用户是否存在
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.USER_NOT_EXIST);
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCodeEnum.USER_PASSWORD_ERROR);
        }

        // 验证账号状态
        if (user.getStatus() == null || user.getStatus() == 0) {
            throw new BusinessException(ResultCodeEnum.USER_ACCOUNT_LOCKED);
        }

        // 更新最后登录信息
        userService.updateLastLoginInfo(user.getId(), ip);

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        log.info("用户登录成功: {}", username);

        return LoginVo.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVo register(RegisterDto request) {
        String username = request.getUsername();
        String email = request.getEmail();

        // 检查用户名是否已存在
        if (userService.getByUsername(username) != null) {
            throw new BusinessException(ResultCodeEnum.USER_ACCOUNT_EXIST);
        }

        // 检查邮箱是否已存在
        if (userService.getByEmail(email) != null) {
            throw new BusinessException(ResultCodeEnum.USER_EMAIL_EXIST);
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(email);
        user.setNickname(request.getNickname() != null ? request.getNickname() : username);
        user.setRole("USER");
        user.setStatus(1);

        boolean saved = userService.save(user);
        if (!saved) {
            throw new BusinessException("注册失败，请稍后重试");
        }

        log.info("用户注册成功: {}", username);

        // 注册成功后自动登录
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        return LoginVo.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .build();
    }
}
