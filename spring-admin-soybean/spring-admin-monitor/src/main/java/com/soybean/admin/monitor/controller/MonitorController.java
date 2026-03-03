package com.soybean.admin.monitor.controller;

import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.monitor.service.SystemMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 系统监控控制器
 */
@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SystemMonitorService systemMonitorService;

    /**
     * 获取服务器信息
     */
    @GetMapping("/server")
    @RequirePermission("monitor:server:list")
    public Result<Map<String, Object>> getServerInfo() {
        return Result.success(systemMonitorService.getServerInfo());
    }

    /**
     * 获取CPU信息
     */
    @GetMapping("/cpu")
    @RequirePermission("monitor:cpu:list")
    public Result<Map<String, Object>> getCpuInfo() {
        return Result.success(systemMonitorService.getCpuInfo());
    }

    /**
     * 获取内存信息
     */
    @GetMapping("/memory")
    @RequirePermission("monitor:memory:list")
    public Result<Map<String, Object>> getMemoryInfo() {
        return Result.success(systemMonitorService.getMemoryInfo());
    }

    /**
     * 获取磁盘信息
     */
    @GetMapping("/disk")
    @RequirePermission("monitor:disk:list")
    public Result<Map<String, Object>> getDiskInfo() {
        return Result.success(systemMonitorService.getDiskInfo());
    }

    /**
     * 获取JVM信息
     */
    @GetMapping("/jvm")
    @RequirePermission("monitor:jvm:list")
    public Result<Map<String, Object>> getJvmInfo() {
        return Result.success(systemMonitorService.getJvmInfo());
    }

    /**
     * 获取网络信息
     */
    @GetMapping("/network")
    @RequirePermission("monitor:network:list")
    public Result<Map<String, Object>> getNetworkInfo() {
        return Result.success(systemMonitorService.getNetworkInfo());
    }

    /**
     * 获取Redis信息
     */
    @GetMapping("/redis")
    @RequirePermission("monitor:redis:list")
    public Result<Map<String, Object>> getRedisInfo() {
        Map<String, Object> redisInfo = new HashMap<>();

        try {
            // 获取Redis信息
            Properties info = (Properties) redisTemplate.getConnectionFactory()
                .getConnection()
                .info();

            redisInfo.put("info", info);

            // 获取数据库大小
            redisInfo.put("dbSize", redisTemplate.getConnectionFactory().getConnection().dbSize());

            // 获取内存信息
            redisInfo.put("usedMemory", info.getProperty("used_memory_human"));

            // 获取连接数
            redisInfo.put("connectedClients", info.getProperty("connected_clients"));

            // 获取Ping结果
            redisInfo.put("ping", "PONG");

        } catch (Exception e) {
            redisInfo.put("error", e.getMessage());
        }

        return Result.success(redisInfo);
    }

    /**
     * 获取缓存监控信息
     */
    @GetMapping("/cache")
    @RequirePermission("monitor:cache:list")
    public Result<Map<String, Object>> getCacheInfo() {
        Map<String, Object> cacheInfo = new HashMap<>();

        try {
            // 获取缓存命令统计
            Properties info = (Properties) redisTemplate.getConnectionFactory()
                .getConnection()
                .info("stats");

            cacheInfo.put("stats", info);

            // 缓存键前缀列表
            cacheInfo.put("cacheNames", new String[]{
                "login:token:",
                "sms:verify_code:",
                "sms:rate_limit:",
                "captcha:",
                "token:blacklist:",
                "login_attempt:",
                "login_lock:",
                "ip:blacklist:"
            });

            // 获取键数量统计
            Map<String, Long> keyCount = new HashMap<>();
            String[] prefixes = {
                "login:token:",
                "sms:verify_code:",
                "captcha:",
                "token:blacklist:"
            };

            for (String prefix : prefixes) {
                Long count = redisTemplate.keys(prefix + "*").stream().count();
                keyCount.put(prefix, count);
            }
            cacheInfo.put("keyCount", keyCount);

        } catch (Exception e) {
            cacheInfo.put("error", e.getMessage());
        }

        return Result.success(cacheInfo);
    }

    /**
     * 获取性能指标
     */
    @GetMapping("/performance")
    @RequirePermission("monitor:performance:list")
    public Result<Map<String, Object>> getPerformanceInfo() {
        Map<String, Object> performanceInfo = new HashMap<>();

        // 系统性能
        performanceInfo.put("cpu", systemMonitorService.getCpuInfo());
        performanceInfo.put("memory", systemMonitorService.getMemoryInfo());
        performanceInfo.put("jvm", systemMonitorService.getJvmInfo());

        // Redis性能
        try {
            Properties stats = (Properties) redisTemplate.getConnectionFactory()
                .getConnection()
                .info("stats");

            Map<String, Object> redisStats = new HashMap<>();
            redisStats.put("commands", stats.getProperty("total_commands_processed"));
            redisStats.put("connections", stats.getProperty("total_connections_received"));
            redisStats.put("keyspaceHits", stats.getProperty("keyspace_hits"));
            redisStats.put("keyspaceMisses", stats.getProperty("keyspace_misses"));

            long hits = Long.parseLong(stats.getProperty("keyspace_hits", "0"));
            long misses = Long.parseLong(stats.getProperty("keyspace_misses", "0"));
            long total = hits + misses;
            double hitRate = total > 0 ? (double) hits / total * 100 : 0;

            redisStats.put("hitRate", String.format("%.2f%%", hitRate));
            performanceInfo.put("redisStats", redisStats);
        } catch (Exception e) {
            performanceInfo.put("redisError", e.getMessage());
        }

        return Result.success(performanceInfo);
    }
}
