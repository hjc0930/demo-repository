package com.soybean.admin.monitor.controller;

import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 在线用户控制器
 */
@RestController
@RequestMapping("/api/monitor/online")
@RequiredArgsConstructor
public class OnlineUserController {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String LOGIN_TOKEN_KEY = "login:token:";

    /**
     * 获取在线用户列表
     */
    @GetMapping("/page")
    @RequirePermission("monitor:online:list")
    public Result<List<Map<String, Object>>> list() {
        List<Map<String, Object>> onlineUsers = new ArrayList<>();

        try {
            // 获取所有登录token
            Set<String> keys = redisTemplate.keys(LOGIN_TOKEN_KEY + "*");
            if (keys != null && !keys.isEmpty()) {
                for (String key : keys) {
                    Map<String, Object> user = new HashMap<>();
                    String token = key.substring(LOGIN_TOKEN_KEY.length());

                    // 获取过期时间
                    Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);

                    user.put("token", token);
                    user.put("expire", expire);

                    // 这里可以添加更多用户信息
                    onlineUsers.add(user);
                }
            }
        } catch (Exception e) {
            // 忽略错误
        }

        return Result.page(onlineUsers, onlineUsers.size());
    }

    /**
     * 强退用户
     */
    @DeleteMapping("/{token}")
    @RequirePermission("monitor:online:forceLogout")
    public Result<Void> forceLogout(@PathVariable String token) {
        String key = LOGIN_TOKEN_KEY + token;
        redisTemplate.delete(key);
        return Result.ok();
    }
}
