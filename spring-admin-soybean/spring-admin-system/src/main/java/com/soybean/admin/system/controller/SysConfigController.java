package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysConfig;
import com.soybean.admin.system.dto.ConfigDTO;
import com.soybean.admin.system.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置控制器
 */
@RestController
@RequestMapping("/api/system/config")
@RequiredArgsConstructor
public class SysConfigController {

    private final SysConfigService configService;

    /**
     * 分页查询系统配置
     */
    @GetMapping("/page")
    @RequirePermission("system:config:list")
    public Result<IPage<SysConfig>> page(Page<SysConfig> page, ConfigDTO query) {
        IPage<SysConfig> result = configService.selectConfigPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询系统配置列表
     */
    @GetMapping("/list")
    @RequirePermission("system:config:list")
    public Result<List<SysConfig>> list(ConfigDTO query) {
        List<SysConfig> list = configService.selectConfigList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询系统配置
     */
    @GetMapping("/{configId}")
    @RequirePermission("system:config:query")
    public Result<SysConfig> getConfig(@PathVariable Long configId) {
        SysConfig config = configService.selectConfigById(configId);
        return Result.ok(config);
    }

    /**
     * 根据配置键名查询配置值
     */
    @GetMapping("/key/{configKey}")
    @RequirePermission("system:config:query")
    public Result<String> getConfigValueByKey(@PathVariable String configKey) {
        String configValue = configService.selectConfigValueByKey(configKey);
        return Result.ok(configValue);
    }

    /**
     * 新增系统配置
     */
    @PostMapping
    @RequirePermission("system:config:add")
    @Log(title = "参数配置", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody ConfigDTO configDTO) {
        configService.insertConfig(configDTO);
        return Result.ok();
    }

    /**
     * 修改系统配置
     */
    @PutMapping
    @RequirePermission("system:config:edit")
    @Log(title = "参数配置", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody ConfigDTO configDTO) {
        configService.updateConfig(configDTO);
        return Result.ok();
    }

    /**
     * 删除系统配置
     */
    @DeleteMapping("/{configId}")
    @RequirePermission("system:config:remove")
    @Log(title = "参数配置", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long configId) {
        configService.deleteConfig(configId);
        return Result.ok();
    }

    /**
     * 校验配置键名是否唯一
     */
    @GetMapping("/check/{configKey}")
    @RequirePermission("system:config:query")
    public Result<Boolean> checkConfigKeyUnique(@PathVariable String configKey) {
        ConfigDTO configDTO = new ConfigDTO();
        configDTO.setConfigKey(configKey);
        boolean result = configService.checkConfigKeyUnique(configDTO);
        return Result.ok(result);
    }

    /**
     * 刷新配置缓存
     */
    @DeleteMapping("/refresh")
    @RequirePermission("system:config:remove")
    @Log(title = "参数配置", businessType = Log.BusinessType.CLEAN)
    public Result<Void> refreshCache() {
        // TODO: 刷新配置缓存逻辑
        return Result.ok();
    }
}
