package com.soybean.admin.security.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCache {

    private final StringRedisTemplate redisTemplate;

    /**
     * 缓存基本的对象
     */
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, toString(value));
    }

    /**
     * 缓存基本的对象，带过期时间
     */
    public <T> void set(String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, toString(value), timeout, timeUnit);
    }

    /**
     * 设置有效时间
     */
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        try {
            return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, timeUnit));
        } catch (Exception e) {
            log.error("设置缓存过期时间失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取缓存的基本对象
     */
    public <T> T get(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? fromString(value, clazz) : null;
    }

    /**
     * 获取缓存的基本对象（返回String）
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除单个对象
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 删除集合对象
     */
    public long delete(Collection<String> keys) {
        Long count = redisTemplate.delete(keys);
        return count != null ? count : 0;
    }

    /**
     * 检查缓存中是否存在该key
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 获取过期时间
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key);
        return expire != null ? expire : -1;
    }

    /**
     * 递增
     */
    public long increment(String key) {
        Long value = redisTemplate.opsForValue().increment(key);
        return value != null ? value : 0;
    }

    /**
     * 递增指定值
     */
    public long increment(String key, long delta) {
        Long value = redisTemplate.opsForValue().increment(key, delta);
        return value != null ? value : 0;
    }

    /**
     * 递减
     */
    public long decrement(String key) {
        Long value = redisTemplate.opsForValue().decrement(key);
        return value != null ? value : 0;
    }

    /**
     * 递减指定值
     */
    public long decrement(String key, long delta) {
        Long value = redisTemplate.opsForValue().decrement(key, delta);
        return value != null ? value : 0;
    }

    /**
     * 对象转字符串
     */
    private String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    /**
     * 字符串转对象
     */
    @SuppressWarnings("unchecked")
    private <T> T fromString(String str, Class<T> clazz) {
        if (str == null) {
            return null;
        }
        if (clazz == String.class) {
            return (T) str;
        }
        if (clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(str);
        }
        if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(str);
        }
        if (clazz == Boolean.class || clazz == boolean.class) {
            return (T) Boolean.valueOf(str);
        }
        if (clazz == Double.class || clazz == double.class) {
            return (T) Double.valueOf(str);
        }
        if (clazz == Float.class || clazz == float.class) {
            return (T) Float.valueOf(str);
        }
        return (T) str;
    }

    // ==================== 兼容性方法（与现有代码兼容） ====================

    /**
     * 缓存对象（兼容性方法，同set）
     */
    public <T> void setCacheObject(String key, T value, int timeout, TimeUnit timeUnit) {
        set(key, value, timeout, timeUnit);
    }

    /**
     * 缓存对象（兼容性方法，long类型）
     */
    public void setCacheObject(String key, long value, int timeout, TimeUnit timeUnit) {
        set(key, value, timeout, timeUnit);
    }

    /**
     * 缓存对象（兼容性方法，Integer类型）
     */
    public void setCacheObject(String key, Integer value, int timeout, TimeUnit timeUnit) {
        set(key, value, timeout, timeUnit);
    }

    /**
     * 缓存Set对象（用于IP黑名单等）
     */
    public void setCacheObject(String key, Set<String> value, int timeout, TimeUnit timeUnit) {
        redisTemplate.delete(key);
        if (value != null && !value.isEmpty()) {
            redisTemplate.opsForSet().add(key, value.toArray(new String[0]));
            expire(key, timeout, timeUnit);
        }
    }

    /**
     * 获取缓存对象（兼容性方法，返回String）
     */
    public String getCacheObject(String key) {
        return get(key);
    }

    /**
     * 获取缓存对象（兼容性方法，返回指定类型）
     */
    @SuppressWarnings("unchecked")
    public <T> T getCacheObject(String key, Class<T> clazz) {
        return get(key, clazz);
    }

    /**
     * 获取Set对象（用于IP黑名单等）
     */
    public Set<String> getCacheSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 删除对象（兼容性方法，同delete）
     */
    public boolean deleteObject(String key) {
        return delete(key);
    }
}
