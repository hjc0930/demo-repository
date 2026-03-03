package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysTenant;
import com.soybean.admin.system.dto.TenantDTO;
import com.soybean.admin.system.service.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户控制器
 */
@RestController
@RequestMapping("/api/system/tenant")
@RequiredArgsConstructor
public class SysTenantController {

    private final SysTenantService tenantService;

    /**
     * 分页查询租户
     */
    @GetMapping("/page")
    @RequirePermission("system:tenant:list")
    public Result<IPage<SysTenant>> page(Page<SysTenant> page, TenantDTO query) {
        IPage<SysTenant> result = tenantService.selectTenantPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询租户列表
     */
    @GetMapping("/list")
    @RequirePermission("system:tenant:list")
    public Result<List<SysTenant>> list(TenantDTO query) {
        List<SysTenant> list = tenantService.selectTenantList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询租户
     */
    @GetMapping("/{id}")
    @RequirePermission("system:tenant:query")
    public Result<SysTenant> getTenant(@PathVariable Long id) {
        SysTenant tenant = tenantService.selectTenantById(id);
        return Result.ok(tenant);
    }

    /**
     * 根据租户ID查询
     */
    @GetMapping("/tenantId/{tenantId}")
    @RequirePermission("system:tenant:query")
    public Result<SysTenant> getTenantByTenantId(@PathVariable String tenantId) {
        SysTenant tenant = tenantService.selectTenantByTenantId(tenantId);
        return Result.ok(tenant);
    }

    /**
     * 新增租户
     */
    @PostMapping
    @RequirePermission("system:tenant:add")
    @Log(title = "租户管理", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody TenantDTO tenantDTO) {
        tenantService.insertTenant(tenantDTO);
        return Result.ok();
    }

    /**
     * 修改租户
     */
    @PutMapping
    @RequirePermission("system:tenant:edit")
    @Log(title = "租户管理", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody TenantDTO tenantDTO) {
        tenantService.updateTenant(tenantDTO);
        return Result.ok();
    }

    /**
     * 删除租户
     */
    @DeleteMapping("/{id}")
    @RequirePermission("system:tenant:remove")
    @Log(title = "租户管理", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return Result.ok();
    }

    /**
     * 校验租户ID是否唯一
     */
    @GetMapping("/check/{tenantId}")
    @RequirePermission("system:tenant:query")
    public Result<Boolean> checkTenantIdUnique(@PathVariable String tenantId) {
        boolean result = tenantService.checkTenantIdUnique(tenantId);
        return Result.ok(result);
    }

    /**
     * 检查租户是否存在且有效
     */
    @GetMapping("/exists/{tenantId}")
    public Result<Boolean> checkTenantExists(@PathVariable String tenantId) {
        boolean result = tenantService.checkTenantExists(tenantId);
        return Result.ok(result);
    }
}
