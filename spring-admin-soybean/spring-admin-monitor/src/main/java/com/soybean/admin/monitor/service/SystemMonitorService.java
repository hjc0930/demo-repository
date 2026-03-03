package com.soybean.admin.monitor.service;

import java.util.Map;

/**
 * 系统监控服务接口
 */
public interface SystemMonitorService {

    /**
     * 获取服务器信息
     */
    Map<String, Object> getServerInfo();

    /**
     * 获取CPU信息
     */
    Map<String, Object> getCpuInfo();

    /**
     * 获取内存信息
     */
    Map<String, Object> getMemoryInfo();

    /**
     * 获取磁盘信息
     */
    Map<String, Object> getDiskInfo();

    /**
     * 获取JVM信息
     */
    Map<String, Object> getJvmInfo();

    /**
     * 获取网络信息
     */
    Map<String, Object> getNetworkInfo();
}
