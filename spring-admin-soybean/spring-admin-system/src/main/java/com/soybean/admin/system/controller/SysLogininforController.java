package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysLogininfor;
import com.soybean.admin.system.dto.LogininforDTO;
import com.soybean.admin.system.service.SysLogininforService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志控制器
 */
@RestController
@RequestMapping("/api/system/logininfor")
@RequiredArgsConstructor
public class SysLogininforController {

    private final SysLogininforService logininforService;

    /**
     * 分页查询登录日志
     */
    @GetMapping("/page")
    @RequirePermission("system:logininfor:list")
    public Result<IPage<SysLogininfor>> page(Page<SysLogininfor> page, LogininforDTO query) {
        IPage<SysLogininfor> result = logininforService.selectLogininforPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询登录日志列表
     */
    @GetMapping("/list")
    @RequirePermission("system:logininfor:list")
    public Result<List<SysLogininfor>> list(LogininforDTO query) {
        List<SysLogininfor> list = logininforService.selectLogininforList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询登录日志
     */
    @GetMapping("/{infoId}")
    @RequirePermission("system:logininfor:query")
    public Result<SysLogininfor> getLogininfor(@PathVariable Long infoId) {
        SysLogininfor logininfor = logininforService.selectLogininforById(infoId);
        return Result.ok(logininfor);
    }

    /**
     * 新增登录日志
     */
    @PostMapping
    public Result<Void> add(@RequestBody LogininforDTO logininforDTO) {
        logininforService.insertLogininfor(logininforDTO);
        return Result.ok();
    }

    /**
     * 删除登录日志
     */
    @DeleteMapping("/{infoId}")
    @RequirePermission("system:logininfor:remove")
    @Log(title = "登录日志", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long infoId) {
        logininforService.deleteLogininfor(infoId);
        return Result.ok();
    }

    /**
     * 批量删除登录日志
     */
    @DeleteMapping("/batch/{infoIds}")
    @RequirePermission("system:logininfor:remove")
    @Log(title = "登录日志", businessType = Log.BusinessType.DELETE)
    public Result<Void> removeBatch(@PathVariable Long[] infoIds) {
        logininforService.deleteLogininforByIds(infoIds);
        return Result.ok();
    }

    /**
     * 清空登录日志
     */
    @DeleteMapping("/clean")
    @RequirePermission("system:logininfor:remove")
    @Log(title = "登录日志", businessType = Log.BusinessType.CLEAN)
    public Result<Void> clean() {
        logininforService.cleanLogininfor();
        return Result.ok();
    }
}
