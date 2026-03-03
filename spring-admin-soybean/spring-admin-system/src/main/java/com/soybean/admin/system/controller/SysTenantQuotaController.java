package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysTenantQuota;
import com.soybean.admin.system.dto.TenantQuotaDTO;
import com.soybean.admin.system.service.SysTenantQuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户配额控制器
 */
@RestController
@RequestMapping("/api/system/tenant/quota")
@RequiredArgsConstructor
public class SysTenantQuotaController {

    private final SysTenantQuotaService tenantQuotaService;

    /**
     * 分页查询租户配额
     */
    @GetMapping("/page")
    @RequirePermission("system:tenant:quota:list")
    public Result<IPage<SysTenantQuota>> page(Page<SysTenantQuota> page, TenantQuotaDTO query) {
        IPage<SysTenantQuota> result = tenantQuotaService.selectTenantQuotaPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询租户配额列表
     */
    @GetMapping("/list")
    @RequirePermission("system:tenant:quota:list")
    public Result<List<SysTenantQuota>> list(TenantQuotaDTO query) {
        List<SysTenantQuota> list = tenantQuotaService.selectTenantQuotaList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询租户配额
     */
    @GetMapping("/{id}")
    @RequirePermission("system:tenant:quota:query")
    public Result<SysTenantQuota> getTenantQuota(@PathVariable Long id) {
        SysTenantQuota tenantQuota = tenantQuotaService.selectTenantQuotaById(id);
        return Result.ok(tenantQuota);
    }

    /**
     * 根据租户ID查询配额
     */
    @GetMapping("/tenant/{tenantId}")
    @RequirePermission("system:tenant:quota:query")
    public Result<SysTenantQuota> getTenantQuotaByTenantId(@PathVariable String tenantId) {
        SysTenantQuota tenantQuota = tenantQuotaService.selectTenantQuotaByTenantId(tenantId);
        return Result.ok(tenantQuota);
    }

    /**
     * 新增租户配额
     */
    @PostMapping
    @RequirePermission("system:tenant:quota:add")
    @Log(title = "租户配额", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody TenantQuotaDTO tenantQuotaDTO) {
        tenantQuotaService.insertTenantQuota(tenantQuotaDTO);
        return Result.ok();
    }

    /**
     * 修改租户配额
     */
    @PutMapping
    @RequirePermission("system:tenant:quota:edit")
    @Log(title = "租户配额", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody TenantQuotaDTO tenantQuotaDTO) {
        tenantQuotaService.updateTenantQuota(tenantQuotaDTO);
        return Result.ok();
    }

    /**
     * 删除租户配额
     */
    @DeleteMapping("/{id}")
    @RequirePermission("system:tenant:quota:remove")
    @Log(title = "租户配额", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long id) {
        tenantQuotaService.deleteTenantQuota(id);
        return Result.ok();
    }

    /**
     * 检查租户配额
     */
    @GetMapping("/check/{tenantId}/{quotaType}")
    @RequirePermission("system:tenant:quota:query")
    public Result<Boolean> checkTenantQuota(@PathVariable String tenantId, @PathVariable String quotaType) {
        boolean result = tenantQuotaService.checkTenantQuota(tenantId, quotaType);
        return Result.ok(result);
    }
}
