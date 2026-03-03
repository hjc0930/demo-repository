package com.soybean.admin.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysTenantPackage;

import java.util.List;
import java.util.Map;

/**
 * 租户套餐服务接口
 */
public interface TenantPackageService extends IService<SysTenantPackage> {

    /**
     * 分页查询租户套餐
     */
    IPage<SysTenantPackage> selectPackagePage(Page<SysTenantPackage> page, String packageName);

    /**
     * 获取套餐功能列表
     */
    Map<String, Object> getPackageFeatures(Long packageId);

    /**
     * 检查套餐是否包含指定功能
     */
    boolean hasFeature(Long packageId, String featureCode);

    /**
     * 获取所有可用套餐
     */
    List<SysTenantPackage> getAvailablePackages();
}
