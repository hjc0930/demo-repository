package com.soybean.admin.security.security;

import com.soybean.admin.security.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * IP黑名单服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IpBlacklistService {

    private final RedisCache redisCache;

    private static final String IP_BLACKLIST_KEY = "security:ip:blacklist";
    private static final int DEFAULT_BLOCK_HOURS = 24;

    /**
     * 添加IP到黑名单
     */
    public void addToBlacklist(String ip, int hours) {
        Set<String> blacklist = redisCache.getCacheSet(IP_BLACKLIST_KEY);

        if (blacklist == null) {
            blacklist = new java.util.concurrent.ConcurrentHashMap<String, Boolean>().newKeySet();
        }

        blacklist.add(ip);
        redisCache.setCacheObject(IP_BLACKLIST_KEY, blacklist, hours, TimeUnit.HOURS);
        log.warn("IP added to blacklist: ip={}, hours={}", ip, hours);
    }

    /**
     * 添加IP到黑名单（默认时长）
     */
    public void addToBlacklist(String ip) {
        addToBlacklist(ip, DEFAULT_BLOCK_HOURS);
    }

    /**
     * 从黑名单移除IP
     */
    public void removeFromBlacklist(String ip) {
        Set<String> blacklist = redisCache.getCacheSet(IP_BLACKLIST_KEY);
        if (blacklist != null) {
            blacklist.remove(ip);
            if (!blacklist.isEmpty()) {
                redisCache.setCacheObject(IP_BLACKLIST_KEY, blacklist, DEFAULT_BLOCK_HOURS, TimeUnit.HOURS);
            } else {
                redisCache.deleteObject(IP_BLACKLIST_KEY);
            }
            log.info("IP removed from blacklist: ip={}", ip);
        }
    }

    /**
     * 检查IP是否在黑名单中
     */
    public boolean isBlacklisted(String ip) {
        Set<String> blacklist = redisCache.getCacheSet(IP_BLACKLIST_KEY);
        return blacklist != null && blacklist.contains(ip);
    }

    /**
     * 获取所有黑名单IP
     */
    public Set<String> getBlacklistedIps() {
        Set<String> blacklist = redisCache.getCacheSet(IP_BLACKLIST_KEY);
        return blacklist != null ? blacklist : Set.of();
    }

    /**
     * 清空黑名单
     */
    public void clearBlacklist() {
        redisCache.deleteObject(IP_BLACKLIST_KEY);
        log.info("IP blacklist cleared");
    }
}
