package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysTenantFeature;
import com.soybean.admin.system.dto.TenantFeatureDTO;
import com.soybean.admin.system.service.SysTenantFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户功能开关控制器
 */
@RestController
@RequestMapping("/api/system/tenant/feature")
@RequiredArgsConstructor
public class SysTenantFeatureController {

    private final SysTenantFeatureService tenantFeatureService;

    /**
     * 分页查询租户功能开关
     */
    @GetMapping("/page")
    @RequirePermission("system:tenant:feature:list")
    public Result<IPage<SysTenantFeature>> page(Page<SysTenantFeature> page, TenantFeatureDTO query) {
        IPage<SysTenantFeature> result = tenantFeatureService.selectTenantFeaturePage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询租户功能开关列表
     */
    @GetMapping("/list")
    @RequirePermission("system:tenant:feature:list")
    public Result<List<SysTenantFeature>> list(TenantFeatureDTO query) {
        List<SysTenantFeature> list = tenantFeatureService.selectTenantFeatureList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询租户功能开关
     */
    @GetMapping("/{id}")
    @RequirePermission("system:tenant:feature:query")
    public Result<SysTenantFeature> getTenantFeature(@PathVariable Long id) {
        SysTenantFeature tenantFeature = tenantFeatureService.selectTenantFeatureById(id);
        return Result.ok(tenantFeature);
    }

    /**
     * 检查功能是否启用
     */
    @GetMapping("/check/{tenantId}/{featureKey}")
    public Result<Boolean> isFeatureEnabled(@PathVariable String tenantId, @PathVariable String featureKey) {
        boolean result = tenantFeatureService.isFeatureEnabled(tenantId, featureKey);
        return Result.ok(result);
    }

    /**
     * 新增租户功能开关
     */
    @PostMapping
    @RequirePermission("system:tenant:feature:add")
    @Log(title = "租户功能开关", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody TenantFeatureDTO tenantFeatureDTO) {
        tenantFeatureService.insertTenantFeature(tenantFeatureDTO);
        return Result.ok();
    }

    /**
     * 修改租户功能开关
     */
    @PutMapping
    @RequirePermission("system:tenant:feature:edit")
    @Log(title = "租户功能开关", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody TenantFeatureDTO tenantFeatureDTO) {
        tenantFeatureService.updateTenantFeature(tenantFeatureDTO);
        return Result.ok();
    }

    /**
     * 删除租户功能开关
     */
    @DeleteMapping("/{id}")
    @RequirePermission("system:tenant:feature:remove")
    @Log(title = "租户功能开关", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long id) {
        tenantFeatureService.deleteTenantFeature(id);
        return Result.ok();
    }

    /**
     * 启用/禁用功能
     */
    @PutMapping("/toggle/{tenantId}/{featureKey}/{enabled}")
    @RequirePermission("system:tenant:feature:edit")
    @Log(title = "租户功能开关", businessType = Log.BusinessType.UPDATE)
    public Result<Void> toggleFeature(@PathVariable String tenantId, @PathVariable String featureKey, @PathVariable Boolean enabled) {
        tenantFeatureService.toggleFeature(tenantId, featureKey, enabled);
        return Result.ok();
    }
}
