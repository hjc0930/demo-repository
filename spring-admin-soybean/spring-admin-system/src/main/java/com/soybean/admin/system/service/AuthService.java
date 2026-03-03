package com.soybean.admin.system.service;

import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysUser;
import com.soybean.admin.data.mapper.SysUserMapper;
import com.soybean.admin.security.captcha.CaptchaService;
import com.soybean.admin.security.details.LoginUser;
import com.soybean.admin.security.security.IpBlacklistService;
import com.soybean.admin.security.security.LoginAttemptService;
import com.soybean.admin.security.utils.JwtUtils;
import com.soybean.admin.system.dto.LoginDto;
import com.soybean.admin.system.dto.LoginResultDto;
import com.soybean.admin.data.tenant.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final SysUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CaptchaService captchaService;
    private final LoginAttemptService loginAttemptService;
    private final IpBlacklistService ipBlacklistService;

    @Value("${security.login.captcha.enabled:true}")
    private boolean captchaEnabled;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            tenantId = "000000";
        }

        SysUser user = userMapper.selectByTenantIdAndUserName(tenantId, username);
        if (user == null) {
            log.error("用户不存在: tenantId={}, username={}", tenantId, username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        if ("1".equals(user.getStatus())) {
            log.error("用户已停用: username={}", username);
            throw new BusinessException(ResponseCode.USER_DISABLED);
        }

        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user, loginUser);
        loginUser.setUserId(user.getUserId());
        loginUser.setUsername(user.getUserName());
        loginUser.setPassword(user.getPassword());

        return loginUser;
    }

    /**
     * 登录
     */
    public LoginResultDto login(LoginDto loginDto) {
        // 获取客户端IP
        String clientIp = getClientIp();

        // 检查IP黑名单
        if (ipBlacklistService.isBlacklisted(clientIp)) {
            log.warn("IP在黑名单中: ip={}", clientIp);
            throw new BusinessException("IP已被禁止访问");
        }

        // 检查是否被锁定
        if (loginAttemptService.isLocked(loginDto.getUsername(), clientIp)) {
            log.warn("账号已锁定: username={}, ip={}", loginDto.getUsername(), clientIp);
            throw new BusinessException("账号已被锁定，请稍后再试");
        }

        // 验证验证码
        if (captchaEnabled) {
            if (loginDto.getUuid() == null || loginDto.getCode() == null) {
                throw new BusinessException("请输入验证码");
            }
            boolean captchaValid = captchaService.verifyCaptcha(loginDto.getUuid(), loginDto.getCode());
            if (!captchaValid) {
                loginAttemptService.loginFailed(loginDto.getUsername(), clientIp);
                throw new BusinessException("验证码错误");
            }
        }

        try {
            // 设置租户上下文
            if (loginDto.getTenantId() != null && !loginDto.getTenantId().isEmpty()) {
                TenantContextHolder.setTenantId(loginDto.getTenantId());
            }

            // 使用Spring Security进行认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
                )
            );

            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            // 记录登录成功
            loginAttemptService.loginSucceeded(loginDto.getUsername(), clientIp);

            // 生成Token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", loginUser.getUserId());
            claims.put("tenantId", loginUser.getTenantId());
            claims.put("username", loginUser.getUsername());

            String accessToken = jwtUtils.generateAccessToken(loginUser.getUsername(), claims);
            String refreshToken = jwtUtils.generateRefreshToken(loginUser.getUsername(), claims);

            // 构建返回结果
            LoginResultDto result = new LoginResultDto();
            result.setAccessToken(accessToken);
            result.setRefreshToken(refreshToken);
            result.setExpiresIn(30 * 60L); // 30分钟

            LoginResultDto.UserInfo userInfo = new LoginResultDto.UserInfo();
            userInfo.setUserId(loginUser.getUserId());
            userInfo.setUsername(loginUser.getUsername());
            userInfo.setNickname(loginUser.getNickname());
            userInfo.setEmail(loginUser.getEmail());
            userInfo.setPhonenumber(loginUser.getPhonenumber());
            userInfo.setAvatar(loginUser.getAvatar());
            userInfo.setTenantId(loginUser.getTenantId());
            result.setUserInfo(userInfo);

            log.info("用户登录成功: username={}, tenantId={}, ip={}", loginDto.getUsername(), loginUser.getTenantId(), clientIp);

            return result;
        } catch (Exception e) {
            // 记录登录失败
            loginAttemptService.loginFailed(loginDto.getUsername(), clientIp);
            throw e;
        }
    }

    /**
     * 刷新Token
     */
    public String refreshToken(String refreshToken) {
        try {
            return jwtUtils.refreshAccessToken(refreshToken);
        } catch (Exception e) {
            log.error("刷新Token失败", e);
            throw new BusinessException(ResponseCode.TOKEN_REFRESH_EXPIRED);
        }
    }

    /**
     * 登出
     */
    public void logout(String token) {
        try {
            jwtUtils.blacklistToken(token);
            log.info("用户登出成功");
        } catch (Exception e) {
            log.error("登出失败", e);
            throw new BusinessException(ResponseCode.OPERATION_FAILED);
        }
    }

    /**
     * 获取当前登录用户信息
     */
    public LoginResultDto.UserInfo getCurrentUserInfo() {
        LoginUser loginUser = getCurrentLoginUser();
        if (loginUser == null) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED);
        }

        LoginResultDto.UserInfo userInfo = new LoginResultDto.UserInfo();
        userInfo.setUserId(loginUser.getUserId());
        userInfo.setUsername(loginUser.getUsername());
        userInfo.setNickname(loginUser.getNickname());
        userInfo.setEmail(loginUser.getEmail());
        userInfo.setPhonenumber(loginUser.getPhonenumber());
        userInfo.setAvatar(loginUser.getAvatar());
        userInfo.setTenantId(loginUser.getTenantId());

        return userInfo;
    }

    /**
     * 获取当前登录用户
     */
    private LoginUser getCurrentLoginUser() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }

                // 处理多个IP的情况，取第一个
                if (ip != null && ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }

                return ip;
            }
        } catch (Exception e) {
            log.error("获取客户端IP失败", e);
        }
        return "unknown";
    }
}
