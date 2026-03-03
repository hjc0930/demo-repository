package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysTenantPackage;
import com.soybean.admin.system.dto.TenantPackageDTO;

import java.util.List;

/**
 * 租户套餐服务接口
 */
public interface SysTenantPackageService extends IService<SysTenantPackage> {

    /**
     * 分页查询租户套餐
     */
    IPage<SysTenantPackage> selectTenantPackagePage(Page<SysTenantPackage> page, TenantPackageDTO query);

    /**
     * 查询租户套餐列表
     */
    List<SysTenantPackage> selectTenantPackageList(TenantPackageDTO query);

    /**
     * 根据ID查询租户套餐
     */
    SysTenantPackage selectTenantPackageById(Integer packageId);

    /**
     * 新增租户套餐
     */
    boolean insertTenantPackage(TenantPackageDTO tenantPackageDTO);

    /**
     * 修改租户套餐
     */
    boolean updateTenantPackage(TenantPackageDTO tenantPackageDTO);

    /**
     * 删除租户套餐
     */
    boolean deleteTenantPackage(Integer packageId);
}
