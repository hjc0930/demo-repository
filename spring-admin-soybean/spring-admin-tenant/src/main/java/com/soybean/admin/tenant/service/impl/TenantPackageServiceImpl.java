package com.soybean.admin.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysTenantPackage;
import com.soybean.admin.data.mapper.SysTenantPackageMapper;
import com.soybean.admin.tenant.service.TenantPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 租户套餐服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantPackageServiceImpl extends ServiceImpl<SysTenantPackageMapper, SysTenantPackage> implements TenantPackageService {

    private final SysTenantPackageMapper tenantPackageMapper;

    @Override
    public IPage<SysTenantPackage> selectPackagePage(Page<SysTenantPackage> page, String packageName) {
        LambdaQueryWrapper<SysTenantPackage> wrapper = new LambdaQueryWrapper<>();

        if (packageName != null && !packageName.isEmpty()) {
            wrapper.like(SysTenantPackage::getPackageName, packageName);
        }

        wrapper.orderByDesc(SysTenantPackage::getCreateTime);
        return tenantPackageMapper.selectPage(page, wrapper);
    }

    @Override
    public Map<String, Object> getPackageFeatures(Long packageId) {
        SysTenantPackage pkg = tenantPackageMapper.selectById(packageId);
        if (pkg == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> features = new HashMap<>();

        // 解析功能配置
        String featureConfig = pkg.getFeatures();
        if (featureConfig != null && !featureConfig.isEmpty()) {
            // 假设features字段存储的是JSON格式的功能配置
            // 格式示例：{"oss": true, "sms": true, "email": false}
            try {
                // 使用JSON解析器解析
                // 这里简化处理，实际应使用Jackson或Gson
                String[] featurePairs = featureConfig.split(",");
                for (String pair : featurePairs) {
                    String[] kv = pair.split(":");
                    if (kv.length == 2) {
                        features.put(kv[0].trim().replaceAll("\"", ""),
                                   Boolean.parseBoolean(kv[1].trim()));
                    }
                }
            } catch (Exception e) {
                log.error("Failed to parse package features: packageId={}", packageId, e);
            }
        }

        // 添加套餐基本信息
        features.put("packageId", pkg.getPackageId());
        features.put("packageName", pkg.getPackageName());
        features.put("maxUsers", pkg.getMaxUsers());
        features.put("maxStorage", pkg.getMaxStorage());
        features.put("price", pkg.getPrice());

        return features;
    }

    @Override
    public boolean hasFeature(Long packageId, String featureCode) {
        Map<String, Object> features = getPackageFeatures(packageId);
        Object enabled = features.get(featureCode);
        return enabled != null && Boolean.TRUE.equals(enabled);
    }

    @Override
    public List<SysTenantPackage> getAvailablePackages() {
        return tenantPackageMapper.selectList(
            new LambdaQueryWrapper<SysTenantPackage>()
                .eq(SysTenantPackage::getStatus, "1")
                .orderByAsc(SysTenantPackage::getPrice)
        );
    }
}
