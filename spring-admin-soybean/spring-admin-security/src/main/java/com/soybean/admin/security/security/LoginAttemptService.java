package com.soybean.admin.security.security;

import com.soybean.admin.security.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 登录尝试限制服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final RedisCache redisCache;

    @Value("${security.login.max-attempts:5}")
    private int maxAttempts;

    @Value("${security.login.lock-time:30}")
    private int lockTimeMinutes;

    private static final String LOGIN_ATTEMPT_KEY_PREFIX = "login_attempt:";
    private static final String LOGIN_LOCK_KEY_PREFIX = "login_lock:";

    /**
     * 记录登录失败
     */
    public void loginFailed(String username, String ip) {
        String key = LOGIN_ATTEMPT_KEY_PREFIX + username + ":" + ip;
        Integer attempts = redisCache.getCacheObject(key, Integer.class);

        if (attempts == null) {
            attempts = 0;
        }

        attempts++;
        redisCache.setCacheObject(key, attempts, lockTimeMinutes, TimeUnit.MINUTES);

        log.warn("Login failed: username={}, ip={}, attempts={}", username, ip, attempts);

        // 达到最大尝试次数，锁定账号
        if (attempts >= maxAttempts) {
            lockUser(username, ip);
        }
    }

    /**
     * 记录登录成功，清除失败记录
     */
    public void loginSucceeded(String username, String ip) {
        String attemptKey = LOGIN_ATTEMPT_KEY_PREFIX + username + ":" + ip;
        String lockKey = LOGIN_LOCK_KEY_PREFIX + username + ":" + ip;

        redisCache.deleteObject(attemptKey);
        redisCache.deleteObject(lockKey);

        log.info("Login succeeded: username={}, ip={}", username, ip);
    }

    /**
     * 检查是否被锁定
     */
    public boolean isLocked(String username, String ip) {
        String lockKey = LOGIN_LOCK_KEY_PREFIX + username + ":" + ip;
        return redisCache.getCacheObject(lockKey) != null;
    }

    /**
     * 获取剩余尝试次数
     */
    public int getRemainingAttempts(String username, String ip) {
        String key = LOGIN_ATTEMPT_KEY_PREFIX + username + ":" + ip;
        Integer attempts = redisCache.getCacheObject(key, Integer.class);

        if (attempts == null) {
            return maxAttempts;
        }

        return Math.max(0, maxAttempts - attempts);
    }

    /**
     * 锁定用户
     */
    private void lockUser(String username, String ip) {
        String lockKey = LOGIN_LOCK_KEY_PREFIX + username + ":" + ip;
        redisCache.setCacheObject(lockKey, System.currentTimeMillis(),
            lockTimeMinutes, TimeUnit.MINUTES);

        log.warn("User locked due to too many failed attempts: username={}, ip={}, lockTime={}min",
            username, ip, lockTimeMinutes);
    }

    /**
     * 解锁用户
     */
    public void unlockUser(String username, String ip) {
        String attemptKey = LOGIN_ATTEMPT_KEY_PREFIX + username + ":" + ip;
        String lockKey = LOGIN_LOCK_KEY_PREFIX + username + ":" + ip;

        redisCache.deleteObject(attemptKey);
        redisCache.deleteObject(lockKey);

        log.info("User unlocked: username={}, ip={}", username, ip);
    }
}
