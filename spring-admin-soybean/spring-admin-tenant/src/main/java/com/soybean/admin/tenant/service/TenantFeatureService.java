package com.soybean.admin.tenant.service;

import java.util.List;
import java.util.Map;

/**
 * 租户功能服务接口
 */
public interface TenantFeatureService {

    /**
     * 获取租户所有功能
     */
    Map<String, Boolean> getTenantFeatures(String tenantId);

    /**
     * 检查租户是否启用了指定功能
     */
    boolean isFeatureEnabled(String tenantId, String featureCode);

    /**
     * 启用租户功能
     */
    boolean enableFeature(String tenantId, String featureCode);

    /**
     * 禁用租户功能
     */
    boolean disableFeature(String tenantId, String featureCode);

    /**
     * 获取所有可用的功能列表
     */
    List<Map<String, Object>> getAvailableFeatures();

    /**
     * 更新租户功能配置
     */
    boolean updateTenantFeatures(String tenantId, Map<String, Boolean> features);
}
