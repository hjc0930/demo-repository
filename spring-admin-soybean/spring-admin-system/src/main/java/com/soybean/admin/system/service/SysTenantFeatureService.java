package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysTenantFeature;
import com.soybean.admin.system.dto.TenantFeatureDTO;

import java.util.List;

/**
 * 租户功能开关服务接口
 */
public interface SysTenantFeatureService extends IService<SysTenantFeature> {

    /**
     * 分页查询租户功能开关
     */
    IPage<SysTenantFeature> selectTenantFeaturePage(Page<SysTenantFeature> page, TenantFeatureDTO query);

    /**
     * 查询租户功能开关列表
     */
    List<SysTenantFeature> selectTenantFeatureList(TenantFeatureDTO query);

    /**
     * 根据ID查询租户功能开关
     */
    SysTenantFeature selectTenantFeatureById(Long id);

    /**
     * 根据租户ID和功能键查询
     */
    SysTenantFeature selectTenantFeature(String tenantId, String featureKey);

    /**
     * 检查功能是否启用
     */
    boolean isFeatureEnabled(String tenantId, String featureKey);

    /**
     * 新增租户功能开关
     */
    boolean insertTenantFeature(TenantFeatureDTO tenantFeatureDTO);

    /**
     * 修改租户功能开关
     */
    boolean updateTenantFeature(TenantFeatureDTO tenantFeatureDTO);

    /**
     * 删除租户功能开关
     */
    boolean deleteTenantFeature(Long id);

    /**
     * 启用/禁用功能
     */
    boolean toggleFeature(String tenantId, String featureKey, boolean enabled);
}
