package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysTenantQuota;
import com.soybean.admin.system.dto.TenantQuotaDTO;

import java.util.List;

/**
 * 租户配额服务接口
 */
public interface SysTenantQuotaService extends IService<SysTenantQuota> {

    /**
     * 分页查询租户配额
     */
    IPage<SysTenantQuota> selectTenantQuotaPage(Page<SysTenantQuota> page, TenantQuotaDTO query);

    /**
     * 查询租户配额列表
     */
    List<SysTenantQuota> selectTenantQuotaList(TenantQuotaDTO query);

    /**
     * 根据ID查询租户配额
     */
    SysTenantQuota selectTenantQuotaById(Long id);

    /**
     * 根据租户ID查询配额
     */
    SysTenantQuota selectTenantQuotaByTenantId(String tenantId);

    /**
     * 新增租户配额
     */
    boolean insertTenantQuota(TenantQuotaDTO tenantQuotaDTO);

    /**
     * 修改租户配额
     */
    boolean updateTenantQuota(TenantQuotaDTO tenantQuotaDTO);

    /**
     * 删除租户配额
     */
    boolean deleteTenantQuota(Long id);

    /**
     * 检查租户配额
     */
    boolean checkTenantQuota(String tenantId, String quotaType);
}
