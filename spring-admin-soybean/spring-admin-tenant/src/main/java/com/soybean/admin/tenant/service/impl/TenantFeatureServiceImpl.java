package com.soybean.admin.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soybean.admin.data.entity.SysTenantFeature;
import com.soybean.admin.data.mapper.SysTenantFeatureMapper;
import com.soybean.admin.tenant.service.TenantFeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 租户功能服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantFeatureServiceImpl implements TenantFeatureService {

    private final SysTenantFeatureMapper tenantFeatureMapper;

    // 可用功能列表
    private static final Map<String, Map<String, Object>> AVAILABLE_FEATURES = new LinkedHashMap<>();

    static {
        // OSS文件存储
        Map<String, Object> oss = new HashMap<>();
        oss.put("code", "oss");
        oss.put("name", "文件存储");
        oss.put("description", "OSS文件上传、下载、预览功能");
        oss.put("category", "storage");
        AVAILABLE_FEATURES.put("oss", oss);

        // 短信服务
        Map<String, Object> sms = new HashMap<>();
        sms.put("code", "sms");
        sms.put("name", "短信服务");
        sms.put("description", "短信验证码、通知短信功能");
        sms.put("category", "message");
        AVAILABLE_FEATURES.put("sms", sms);

        // 邮件服务
        Map<String, Object> email = new HashMap<>();
        email.put("code", "email");
        email.put("name", "邮件服务");
        email.put("description", "邮件发送、模板管理功能");
        email.put("category", "message");
        AVAILABLE_FEATURES.put("email", email);

        // 定时任务
        Map<String, Object> job = new HashMap<>();
        job.put("code", "job");
        job.put("name", "定时任务");
        job.put("description", "Quartz定时任务调度功能");
        job.put("category", "system");
        AVAILABLE_FEATURES.put("job", job);

        // 代码生成
        Map<String, Object> generator = new HashMap<>();
        generator.put("code", "generator");
        generator.put("name", "代码生成");
        generator.put("description", "代码自动生成功能");
        generator.put("category", "developer");
        AVAILABLE_FEATURES.put("generator", generator);

        // OAuth2登录
        Map<String, Object> oauth2 = new HashMap<>();
        oauth2.put("code", "oauth2");
        oauth2.put("name", "OAuth2登录");
        oauth2.put("description", "第三方账号登录功能");
        oauth2.put("category", "security");
        AVAILABLE_FEATURES.put("oauth2", oauth2);

        // 系统监控
        Map<String, Object> monitor = new HashMap<>();
        monitor.put("code", "monitor");
        monitor.put("name", "系统监控");
        monitor.put("description", "服务器、JVM、Redis监控功能");
        monitor.put("category", "system");
        AVAILABLE_FEATURES.put("monitor", monitor);

        // 站内消息
        Map<String, Object> notification = new HashMap<>();
        notification.put("code", "notification");
        notification.put("name", "站内消息");
        notification.put("description", "WebSocket实时消息推送");
        notification.put("category", "message");
        AVAILABLE_FEATURES.put("notification", notification);

        // 数据权限
        Map<String, Object> dataPermission = new HashMap<>();
        dataPermission.put("code", "data_permission");
        dataPermission.put("name", "数据权限");
        dataPermission.put("description", "基于角色的数据权限控制");
        dataPermission.put("category", "security");
        AVAILABLE_FEATURES.put("data_permission", dataPermission);

        // 操作日志
        Map<String, Object> auditLog = new HashMap<>();
        auditLog.put("code", "audit_log");
        auditLog.put("name", "操作日志");
        auditLog.put("description", "用户操作审计日志");
        auditLog.put("category", "system");
        AVAILABLE_FEATURES.put("audit_log", auditLog);
    }

    @Override
    public Map<String, Boolean> getTenantFeatures(String tenantId) {
        List<SysTenantFeature> features = tenantFeatureMapper.selectList(
            new LambdaQueryWrapper<SysTenantFeature>()
                .eq(SysTenantFeature::getTenantId, tenantId)
        );

        Map<String, Boolean> featureMap = new HashMap<>();

        // 默认所有功能禁用
        for (String featureCode : AVAILABLE_FEATURES.keySet()) {
            featureMap.put(featureCode, false);
        }

        // 更新实际功能状态
        for (SysTenantFeature feature : features) {
            featureMap.put(feature.getFeatureCode(), "1".equals(feature.getEnabled()));
        }

        return featureMap;
    }

    @Override
    public boolean isFeatureEnabled(String tenantId, String featureCode) {
        SysTenantFeature feature = tenantFeatureMapper.selectOne(
            new LambdaQueryWrapper<SysTenantFeature>()
                .eq(SysTenantFeature::getTenantId, tenantId)
                .eq(SysTenantFeature::getFeatureCode, featureCode)
        );

        return feature != null && "1".equals(feature.getEnabled());
    }

    @Override
    public boolean enableFeature(String tenantId, String featureCode) {
        SysTenantFeature feature = getOrCreateFeature(tenantId, featureCode);
        feature.setEnabledFromString("1");
        return tenantFeatureMapper.updateById(feature) > 0;
    }

    @Override
    public boolean disableFeature(String tenantId, String featureCode) {
        SysTenantFeature feature = getOrCreateFeature(tenantId, featureCode);
        feature.setEnabledFromString("0");
        return tenantFeatureMapper.updateById(feature) > 0;
    }

    @Override
    public List<Map<String, Object>> getAvailableFeatures() {
        return new ArrayList<>(AVAILABLE_FEATURES.values());
    }

    @Override
    public boolean updateTenantFeatures(String tenantId, Map<String, Boolean> features) {
        for (Map.Entry<String, Boolean> entry : features.entrySet()) {
            SysTenantFeature feature = getOrCreateFeature(tenantId, entry.getKey());
            feature.setEnabledFromString(entry.getValue() ? "1" : "0");
            tenantFeatureMapper.updateById(feature);
        }
        return true;
    }

    /**
     * 获取或创建功能记录
     */
    private SysTenantFeature getOrCreateFeature(String tenantId, String featureCode) {
        SysTenantFeature feature = tenantFeatureMapper.selectOne(
            new LambdaQueryWrapper<SysTenantFeature>()
                .eq(SysTenantFeature::getTenantId, tenantId)
                .eq(SysTenantFeature::getFeatureCode, featureCode)
        );

        if (feature == null) {
            Map<String, Object> featureInfo = AVAILABLE_FEATURES.get(featureCode);
            if (featureInfo == null) {
                throw new RuntimeException("Unknown feature code: " + featureCode);
            }

            feature = new SysTenantFeature();
            feature.setTenantId(tenantId);
            feature.setFeatureCode(featureCode);
            feature.setFeatureName((String) featureInfo.get("name"));
            feature.setEnabledFromString("0");

            tenantFeatureMapper.insert(feature);
        }

        return feature;
    }
}
