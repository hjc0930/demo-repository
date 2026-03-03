package com.soybean.admin.oauth2.handler;

import com.soybean.admin.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2认证成功处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 获取用户信息
        String provider = getProvider(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String username = extractUsername(provider, attributes);
        String email = (String) attributes.get("email");

        // 生成JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("provider", provider);
        claims.put("email", email);
        claims.put("name", username);

        String accessToken = jwtUtils.generateAccessToken(username, claims);

        // 重定向到前端，携带token
        String redirectUrl = "/oauth2/callback?token=" + accessToken;
        response.sendRedirect(redirectUrl);

        log.info("OAuth2 login success: provider={}, username={}", provider, username);
    }

    /**
     * 从请求中获取OAuth2提供商
     */
    private String getProvider(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("github")) {
            return "github";
        } else if (uri.contains("gitee")) {
            return "gitee";
        } else if (uri.contains("wechat")) {
            return "wechat";
        }
        return "unknown";
    }

    /**
     * 从属性中提取用户名
     */
    private String extractUsername(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "github":
                return (String) attributes.get("login");
            case "gitee":
                return (String) attributes.get("login");
            case "wechat":
                return (String) attributes.get("nickname");
            default:
                return (String) attributes.get("name");
        }
    }
}
