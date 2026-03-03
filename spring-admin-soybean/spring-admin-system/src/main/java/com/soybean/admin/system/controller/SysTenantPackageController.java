package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysTenantPackage;
import com.soybean.admin.system.dto.TenantPackageDTO;
import com.soybean.admin.system.service.SysTenantPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户套餐控制器
 */
@RestController
@RequestMapping("/api/system/tenant/package")
@RequiredArgsConstructor
public class SysTenantPackageController {

    private final SysTenantPackageService tenantPackageService;

    /**
     * 分页查询租户套餐
     */
    @GetMapping("/page")
    @RequirePermission("system:tenant:package:list")
    public Result<IPage<SysTenantPackage>> page(Page<SysTenantPackage> page, TenantPackageDTO query) {
        IPage<SysTenantPackage> result = tenantPackageService.selectTenantPackagePage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询租户套餐列表
     */
    @GetMapping("/list")
    @RequirePermission("system:tenant:package:list")
    public Result<List<SysTenantPackage>> list(TenantPackageDTO query) {
        List<SysTenantPackage> list = tenantPackageService.selectTenantPackageList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询租户套餐
     */
    @GetMapping("/{packageId}")
    @RequirePermission("system:tenant:package:query")
    public Result<SysTenantPackage> getTenantPackage(@PathVariable Integer packageId) {
        SysTenantPackage tenantPackage = tenantPackageService.selectTenantPackageById(packageId);
        return Result.ok(tenantPackage);
    }

    /**
     * 新增租户套餐
     */
    @PostMapping
    @RequirePermission("system:tenant:package:add")
    @Log(title = "租户套餐", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody TenantPackageDTO tenantPackageDTO) {
        tenantPackageService.insertTenantPackage(tenantPackageDTO);
        return Result.ok();
    }

    /**
     * 修改租户套餐
     */
    @PutMapping
    @RequirePermission("system:tenant:package:edit")
    @Log(title = "租户套餐", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody TenantPackageDTO tenantPackageDTO) {
        tenantPackageService.updateTenantPackage(tenantPackageDTO);
        return Result.ok();
    }

    /**
     * 删除租户套餐
     */
    @DeleteMapping("/{packageId}")
    @RequirePermission("system:tenant:package:remove")
    @Log(title = "租户套餐", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Integer packageId) {
        tenantPackageService.deleteTenantPackage(packageId);
        return Result.ok();
    }
}
