package com.soybean.admin.common.aspect;

import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 幂等性切面
 * 基于Redis实现接口幂等性校验
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final StringRedisTemplate redisTemplate;

    /**
     * 幂等性校验，requestKey由注解指定spel表达式生成
     */
    @Around("@annotation(com.soybean.admin.common.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        com.soybean.admin.common.annotation.Idempotent idempotent =
            ((MethodSignature) point.getSignature()).getMethod()
                .getAnnotation(com.soybean.admin.common.annotation.Idempotent.class);

        String key = generateKey(point, idempotent);

        // 尝试设置锁，如果已存在则抛出重复请求异常
        Boolean success = redisTemplate.opsForValue()
            .setIfAbsent(key, "1", idempotent.timeout(), TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(success)) {
            log.warn("重复请求，key: {}", key);
            throw new BusinessException(ResponseCode.OPERATION_FAILED, "请勿重复提交");
        }

        try {
            // 执行业务逻辑
            return point.proceed();
        } finally {
            // 如果注解配置了执行后删除，则删除key
            if (idempotent.delKey()) {
                redisTemplate.delete(key);
            }
        }
    }

    /**
     * 生成幂等性key
     */
    private String generateKey(ProceedingJoinPoint point, com.soybean.admin.common.annotation.Idempotent idempotent) {
        String prefix = idempotent.prefix();
        if (prefix == null || prefix.isEmpty()) {
            prefix = "idempotent:";
        }

        String key;
        if (!idempotent.key().isEmpty()) {
            // 使用SpEL表达式生成key
            key = parseSpEL(idempotent.key(), point);
        } else {
            key = idempotent.key();
        }

        // 添加当前用户标识（如果有）
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
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
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

    /**
     * 删除key
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 检查key是否存在
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
