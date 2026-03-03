package com.soybean.admin.system.dto;

import lombok.Data;

/**
 * 登录结果DTO
 */
@Data
public class LoginResultDto {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";

    /**
     * 过期时间（秒）
     */
    private Long expiresIn;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    @Data
    public static class UserInfo {
        private Long userId;
        private String username;
        private String nickname;
        private String email;
        private String phonenumber;
        private String avatar;
        private String tenantId;
    }
}
