package com.soybean.admin.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soybean.admin.data.entity.SysTenantQuota;
import com.soybean.admin.data.mapper.SysTenantQuotaMapper;
import com.soybean.admin.tenant.service.TenantQuotaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户配额服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantQuotaServiceImpl implements TenantQuotaService {

    private final SysTenantQuotaMapper tenantQuotaMapper;

    @Override
    public Map<String, Object> getTenantQuota(String tenantId) {
        List<SysTenantQuota> quotas = tenantQuotaMapper.selectList(
            new LambdaQueryWrapper<SysTenantQuota>()
                .eq(SysTenantQuota::getTenantId, tenantId)
        );

        Map<String, Object> quotaMap = new HashMap<>();

        for (SysTenantQuota quota : quotas) {
            Map<String, Object> quotaInfo = new HashMap<>();
            quotaInfo.put("limit", quota.getQuotaLimit());
            quotaInfo.put("used", quota.getQuotaUsed());
            quotaInfo.put("remaining", quota.getQuotaLimit() - quota.getQuotaUsed());
            quotaInfo.put("usagePercent", (double) quota.getQuotaUsed() / quota.getQuotaLimit() * 100);

            quotaMap.put(quota.getQuotaType(), quotaInfo);
        }

        return quotaMap;
    }

    @Override
    public boolean checkQuota(String tenantId, String quotaType) {
        SysTenantQuota quota = tenantQuotaMapper.selectOne(
            new LambdaQueryWrapper<SysTenantQuota>()
                .eq(SysTenantQuota::getTenantId, tenantId)
                .eq(SysTenantQuota::getQuotaType, quotaType)
        );

        if (quota == null) {
            log.warn("Quota not found: tenantId={}, quotaType={}", tenantId, quotaType);
            return false;
        }

        return quota.getQuotaUsed() < quota.getQuotaLimit();
    }

    @Override
    public boolean incrementUsage(String tenantId, String quotaType, int count) {
        SysTenantQuota quota = getOrCreateQuota(tenantId, quotaType);

        if (quota.getQuotaUsed() + count > quota.getQuotaLimit()) {
            log.warn("Quota exceeded: tenantId={}, quotaType={}, used={}, limit={}",
                tenantId, quotaType, quota.getQuotaUsed(), quota.getQuotaLimit());
            return false;
        }

        quota.setQuotaUsed(quota.getQuotaUsed() + count);
        tenantQuotaMapper.updateById(quota);
        return true;
    }

    @Override
    public boolean decrementUsage(String tenantId, String quotaType, int count) {
        SysTenantQuota quota = getOrCreateQuota(tenantId, quotaType);

        quota.setQuotaUsed(Math.max(0, quota.getQuotaUsed() - count));
        tenantQuotaMapper.updateById(quota);
        return true;
    }

    @Override
    public boolean resetQuota(String tenantId, String quotaType) {
        SysTenantQuota quota = getOrCreateQuota(tenantId, quotaType);
        quota.setQuotaUsed(0);
        tenantQuotaMapper.updateById(quota);
        return true;
    }

    @Override
    public double getQuotaUsagePercent(String tenantId, String quotaType) {
        SysTenantQuota quota = tenantQuotaMapper.selectOne(
            new LambdaQueryWrapper<SysTenantQuota>()
                .eq(SysTenantQuota::getTenantId, tenantId)
                .eq(SysTenantQuota::getQuotaType, quotaType)
        );

        if (quota == null) {
            return 0;
        }

        return quota.getQuotaLimit() > 0
            ? (double) quota.getQuotaUsed() / quota.getQuotaLimit() * 100
            : 0;
    }

    /**
     * 获取或创建配额记录
     */
    private SysTenantQuota getOrCreateQuota(String tenantId, String quotaType) {
        SysTenantQuota quota = tenantQuotaMapper.selectOne(
            new LambdaQueryWrapper<SysTenantQuota>()
                .eq(SysTenantQuota::getTenantId, tenantId)
                .eq(SysTenantQuota::getQuotaType, quotaType)
        );

        if (quota == null) {
            quota = new SysTenantQuota();
            quota.setTenantId(tenantId);
            quota.setQuotaType(quotaType);
            quota.setQuotaUsed(0L);

            // 根据配额类型设置默认限制
            switch (quotaType) {
                case "users":
                    quota.setQuotaLimit(10L);
                    break;
                case "storage":
                    quota.setQuotaLimit(10737418240L); // 10GB
                    break;
                case "api_calls":
                    quota.setQuotaLimit(10000L);
                    break;
                default:
                    quota.setQuotaLimit(1000L);
            }

            tenantQuotaMapper.insert(quota);
        }

        return quota;
    }
}
