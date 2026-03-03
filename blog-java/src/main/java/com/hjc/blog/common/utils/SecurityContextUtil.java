package com.hjc.blog.common.utils;

import com.hjc.blog.security.user.BlogUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Security 工具类
 * 用于获取当前登录用户信息
 */
@UtilityClass
public class SecurityContextUtil {

    /**
     * 获取当前认证信息
     *
     * @return Authentication，未登录返回 null
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 判断用户是否已登录
     *
     * @return true-已登录，false-未登录
     */
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * 获取当前用户 ID
     *
     * @return 用户ID，未登录返回 null
     */
    public Long getUserId() {
        BlogUserDetails userDetails = getBlogUserDetails();
        return userDetails != null ? userDetails.getUserId() : null;
    }

    /**
     * 获取当前用户 ID（Optional 版本）
     *
     * @return Optional<Long>
     */
    public Optional<Long> getUserIdOptional() {
        return Optional.ofNullable(getUserId());
    }

    /**
     * 获取当前用户 ID，如果未登录则抛出异常
     *
     * @return 用户ID
     * @throws IllegalStateException 用户未登录
     */
    public Long getRequiredUserId() {
        Long userId = getUserId();
        if (userId == null) {
            throw new IllegalStateException("用户未登录");
        }
        return userId;
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名，未登录返回 null
     */
    public String getUsername() {
        BlogUserDetails userDetails = getBlogUserDetails();
        return userDetails != null ? userDetails.getUsername() : null;
    }

    /**
     * 获取当前用户名（Optional 版本）
     *
     * @return Optional<String>
     */
    public Optional<String> getUsernameOptional() {
        return Optional.ofNullable(getUsername());
    }

    /**
     * 获取当前用户名，如果未登录则抛出异常
     *
     * @return 用户名
     * @throws IllegalStateException 用户未登录
     */
    public String getRequiredUsername() {
        String username = getUsername();
        if (username == null) {
            throw new IllegalStateException("用户未登录");
        }
        return username;
    }

    /**
     * 获取当前用户详细信息
     *
     * @return BlogUserDetails，未登录返回 null
     */
    public BlogUserDetails getBlogUserDetails() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof BlogUserDetails) {
            return (BlogUserDetails) principal;
        }
        return null;
    }

    /**
     * 获取当前用户详细信息（Optional 版本）
     *
     * @return Optional<BlogUserDetails>
     */
    public Optional<BlogUserDetails> getBlogUserDetailsOptional() {
        return Optional.ofNullable(getBlogUserDetails());
    }

    /**
     * 获取当前用户角色
     *
     * @return 角色（如 ROLE_ADMIN），未登录返回 null
     */
    public String getRole() {
        BlogUserDetails userDetails = getBlogUserDetails();
        return userDetails != null ? userDetails.getRole() : null;
    }

    /**
     * 获取当前用户角色（Optional 版本）
     *
     * @return Optional<String>
     */
    public Optional<String> getRoleOptional() {
        return Optional.ofNullable(getRole());
    }

    /**
     * 清除当前认证信息（登出）
     */
    public void clear() {
        SecurityContextHolder.clearContext();
    }

    /**
     * 检查当前用户是否有指定角色
     *
     * @param role 角色（如 ROLE_ADMIN）
     * @return true-有该角色，false-没有
     */
    public boolean hasRole(String role) {
        BlogUserDetails userDetails = getBlogUserDetails();
        if (userDetails == null || userDetails.getAuthorities() == null) {
            return false;
        }
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    /**
     * 检查当前用户是否有任意一个指定角色
     *
     * @param roles 角色数组
     * @return true-有任一角色，false-没有
     */
    public boolean hasAnyRole(String... roles) {
        BlogUserDetails userDetails = getBlogUserDetails();
        if (userDetails == null || userDetails.getAuthorities() == null || roles == null) {
            return false;
        }
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> {
                    for (String role : roles) {
                        if (authority.getAuthority().equals(role)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    /**
     * 检查当前用户是否为管理员
     *
     * @return true-是管理员，false-不是
     */
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    /**
     * 检查当前用户是否为普通用户
     *
     * @return true-是普通用户，false-不是
     */
    public boolean isUser() {
        return hasRole("ROLE_USER");
    }
}
