package com.soybean.admin.oauth2.controller;

import com.soybean.admin.common.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2 控制器
 */
@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    @Value("${oauth2.github.client-id:}")
    private String githubClientId;

    @Value("${oauth2.gitee.client-id:}")
    private String giteeClientId;

    @Value("${oauth2.wechat.client-id:}")
    private String wechatClientId;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    /**
     * 获取OAuth2登录配置
     */
    @GetMapping("/config")
    public Result<Map<String, Object>> getOAuth2Config() {
        Map<String, Object> config = new HashMap<>();

        Map<String, String> github = new HashMap<>();
        github.put("clientId", githubClientId);
        github.put("authUrl", "https://github.com/login/oauth/authorize");
        github.put("redirectUri", frontendUrl + "/callback/github");
        config.put("github", github);

        Map<String, String> gitee = new HashMap<>();
        gitee.put("clientId", giteeClientId);
        gitee.put("authUrl", "https://gitee.com/oauth/authorize");
        gitee.put("redirectUri", frontendUrl + "/callback/gitee");
        config.put("gitee", gitee);

        Map<String, String> wechat = new HashMap<>();
        wechat.put("clientId", wechatClientId);
        wechat.put("authUrl", "https://open.weixin.qq.com/connect/qrconnect");
        wechat.put("redirectUri", frontendUrl + "/callback/wechat");
        config.put("wechat", wechat);

        return Result.success(config);
    }

    /**
     * 获取支持的第三方登录列表
     */
    @GetMapping("/providers")
    public Result<Map<String, Boolean>> getProviders() {
        Map<String, Boolean> providers = new HashMap<>();
        providers.put("github", !githubClientId.isEmpty());
        providers.put("gitee", !giteeClientId.isEmpty());
        providers.put("wechat", !wechatClientId.isEmpty());
        return Result.success(providers);
    }
}
