package com.soybean.admin.common.aspect;

import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.Method;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面
 * 基于Redisson实现分布式锁
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    /**
     * 环绕通知，获取分布式锁并执行业务逻辑
     */
    @Around("@annotation(com.soybean.admin.common.annotation.Lock)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        com.soybean.admin.common.annotation.Lock lock =
            ((MethodSignature) point.getSignature()).getMethod()
                .getAnnotation(com.soybean.admin.common.annotation.Lock.class);

        String lockKey = getLockKey(point, lock);
        RLock rLock = redissonClient.getLock(lockKey);

        // 尝试获取锁
        boolean acquired = rLock.tryLock(lock.waitTime(), lock.unit());
        if (!acquired) {
            throw new BusinessException(ResponseCode.OPERATION_FAILED, "操作频繁，请稍后再试");
        }

        try {
            // 执行业务逻辑
            return point.proceed();
        } finally {
            // 释放锁
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    /**
     * 获取锁的key
     */
    private String getLockKey(ProceedingJoinPoint point, com.soybean.admin.common.annotation.Lock lock) {
        String prefix = lock.prefix();
        if (prefix == null || prefix.isEmpty()) {
            prefix = "lock:";
        }

        String key;
        if (!lock.key().isEmpty()) {
            // 使用SpEL表达式生成key
            key = parseSpEL(lock.key(), point);
        } else {
            key = lock.key();
        }

        // 添加用户标识（如果有）
        String userKey = getUserKey();
        if (userKey != null) {
            key = prefix + userKey + ":" + key;
        } else {
            key = prefix + key;
        }

        return key;
    }

    /**
     * 解析SpEL表达式
     */
    private String parseSpEL(String spEL, ProceedingJoinPoint point) {
        org.springframework.core.DefaultParameterNameDiscoverer discoverer =
            new org.springframework.core.DefaultParameterNameDiscoverer();
        Method method = ((org.aspectj.lang.reflect.MethodSignature) point.getSignature()).getMethod();
        Object[] args = point.getArgs();
        String[] parameterNames = discoverer.getParameterNames(method);

        String result = spEL;
        for (int i = 0; i < parameterNames.length; i++) {
            String placeholder = "#" + parameterNames[i];
            if (args[i] != null && result.contains(placeholder)) {
                result = result.replace(placeholder, args[i].toString());
            }
        }
        return result;
    }

    /**
     * 获取用户标识
     */
    private String getUserKey() {
        // TODO: 从SecurityContext获取当前用户ID
        return null;
    }
}
