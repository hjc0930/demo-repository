package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysTenant;
import com.soybean.admin.system.dto.TenantDTO;

import java.util.List;

/**
 * 租户服务接口
 */
public interface SysTenantService extends IService<SysTenant> {

    /**
     * 分页查询租户
     */
    IPage<SysTenant> selectTenantPage(Page<SysTenant> page, TenantDTO query);

    /**
     * 查询租户列表
     */
    List<SysTenant> selectTenantList(TenantDTO query);

    /**
     * 根据ID查询租户
     */
    SysTenant selectTenantById(Long id);

    /**
     * 根据租户ID查询
     */
    SysTenant selectTenantByTenantId(String tenantId);

    /**
     * 新增租户
     */
    boolean insertTenant(TenantDTO tenantDTO);

    /**
     * 修改租户
     */
    boolean updateTenant(TenantDTO tenantDTO);

    /**
     * 删除租户
     */
    boolean deleteTenant(Long id);

    /**
     * 校验租户ID是否唯一
     */
    boolean checkTenantIdUnique(String tenantId);

    /**
     * 检查租户是否存在且有效
     */
    boolean checkTenantExists(String tenantId);
}
