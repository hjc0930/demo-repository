package com.soybean.admin.tenant.service;

import com.soybean.admin.data.entity.SysTenantQuota;

import java.util.Map;

/**
 * 租户配额服务接口
 */
public interface TenantQuotaService {

    /**
     * 获取租户配额信息
     */
    Map<String, Object> getTenantQuota(String tenantId);

    /**
     * 检查租户是否超过配额限制
     */
    boolean checkQuota(String tenantId, String quotaType);

    /**
     * 增加配额使用量
     */
    boolean incrementUsage(String tenantId, String quotaType, int count);

    /**
     * 减少配额使用量
     */
    boolean decrementUsage(String tenantId, String quotaType, int count);

    /**
     * 重置租户配额
     */
    boolean resetQuota(String tenantId, String quotaType);

    /**
     * 获取配额使用百分比
     */
    double getQuotaUsagePercent(String tenantId, String quotaType);
}
