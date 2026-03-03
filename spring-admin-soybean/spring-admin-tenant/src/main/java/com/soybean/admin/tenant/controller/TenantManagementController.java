package com.soybean.admin.tenant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.response.PageResult;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysTenantPackage;
import com.soybean.admin.tenant.service.TenantFeatureService;
import com.soybean.admin.tenant.service.TenantPackageService;
import com.soybean.admin.tenant.service.TenantQuotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 租户管理控制器
 */
@RestController
@RequestMapping("/api/system/tenant")
@RequiredArgsConstructor
public class TenantManagementController {

    private final TenantPackageService tenantPackageService;
    private final TenantQuotaService tenantQuotaService;
    private final TenantFeatureService tenantFeatureService;

    // ==================== 租户套餐管理 ====================

    /**
     * 分页查询租户套餐
     */
    @GetMapping("/package/list")
    public Result<PageResult<SysTenantPackage>> listPackage(
            @RequestParam(required = false) String packageName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SysTenantPackage> page = new Page<>(pageNum, pageSize);
        IPage<SysTenantPackage> result = tenantPackageService.selectPackagePage(page, packageName);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取套餐详情
     */
    @GetMapping("/package/{id}")
    public Result<Map<String, Object>> getPackage(@PathVariable Long id) {
        return Result.success(tenantPackageService.getPackageFeatures(id));
    }

    /**
     * 获取所有可用套餐
     */
    @GetMapping("/package/available")
    public Result<List<SysTenantPackage>> getAvailablePackages() {
        return Result.success(tenantPackageService.getAvailablePackages());
    }

    /**
     * 检查套餐是否包含指定功能
     */
    @GetMapping("/package/{packageId}/feature/{featureCode}")
    public Result<Boolean> hasFeature(@PathVariable Long packageId, @PathVariable String featureCode) {
        return Result.success(tenantPackageService.hasFeature(packageId, featureCode));
    }

    // ==================== 租户配额管理 ====================

    /**
     * 获取租户配额信息
     */
    @GetMapping("/quota")
    public Result<Map<String, Object>> getQuota(@RequestParam String tenantId) {
        return Result.success(tenantQuotaService.getTenantQuota(tenantId));
    }

    /**
     * 检查配额是否超限
     */
    @GetMapping("/quota/check")
    public Result<Boolean> checkQuota(@RequestParam String tenantId, @RequestParam String quotaType) {
        return Result.success(tenantQuotaService.checkQuota(tenantId, quotaType));
    }

    /**
     * 获取配额使用百分比
     */
    @GetMapping("/quota/usage-percent")
    public Result<Double> getQuotaUsagePercent(@RequestParam String tenantId, @RequestParam String quotaType) {
        return Result.success(tenantQuotaService.getQuotaUsagePercent(tenantId, quotaType));
    }

    /**
     * 重置配额
     */
    @PostMapping("/quota/reset")
    public Result<Void> resetQuota(@RequestParam String tenantId, @RequestParam String quotaType) {
        tenantQuotaService.resetQuota(tenantId, quotaType);
        return Result.success();
    }

    // ==================== 租户功能管理 ====================

    /**
     * 获取租户功能配置
     */
    @GetMapping("/feature")
    public Result<Map<String, Boolean>> getTenantFeatures(@RequestParam String tenantId) {
        return Result.success(tenantFeatureService.getTenantFeatures(tenantId));
    }

    /**
     * 检查租户功能是否启用
     */
    @GetMapping("/feature/check")
    public Result<Boolean> isFeatureEnabled(@RequestParam String tenantId, @RequestParam String featureCode) {
        return Result.success(tenantFeatureService.isFeatureEnabled(tenantId, featureCode));
    }

    /**
     * 获取所有可用功能
     */
    @GetMapping("/feature/available")
    public Result<List<Map<String, Object>>> getAvailableFeatures() {
        return Result.success(tenantFeatureService.getAvailableFeatures());
    }

    /**
     * 启用租户功能
     */
    @PostMapping("/feature/enable")
    public Result<Void> enableFeature(@RequestParam String tenantId, @RequestParam String featureCode) {
        tenantFeatureService.enableFeature(tenantId, featureCode);
        return Result.success();
    }

    /**
     * 禁用租户功能
     */
    @PostMapping("/feature/disable")
    public Result<Void> disableFeature(@RequestParam String tenantId, @RequestParam String featureCode) {
        tenantFeatureService.disableFeature(tenantId, featureCode);
        return Result.success();
    }

    /**
     * 批量更新租户功能
     */
    @PostMapping("/feature/batch")
    public Result<Void> updateTenantFeatures(@RequestParam String tenantId,
                                              @RequestBody Map<String, Boolean> features) {
        tenantFeatureService.updateTenantFeatures(tenantId, features);
        return Result.success();
    }
}
