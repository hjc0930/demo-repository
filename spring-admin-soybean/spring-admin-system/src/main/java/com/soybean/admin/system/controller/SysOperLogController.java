package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysOperLog;
import com.soybean.admin.system.dto.OperLogDTO;
import com.soybean.admin.system.service.SysOperLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志控制器
 */
@RestController
@RequestMapping("/api/system/operlog")
@RequiredArgsConstructor
public class SysOperLogController {

    private final SysOperLogService operLogService;

    /**
     * 分页查询操作日志
     */
    @GetMapping("/page")
    @RequirePermission("system:operlog:list")
    public Result<IPage<SysOperLog>> page(Page<SysOperLog> page, OperLogDTO query) {
        IPage<SysOperLog> result = operLogService.selectOperLogPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询操作日志列表
     */
    @GetMapping("/list")
    @RequirePermission("system:operlog:list")
    public Result<List<SysOperLog>> list(OperLogDTO query) {
        List<SysOperLog> list = operLogService.selectOperLogList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询操作日志
     */
    @GetMapping("/{operId}")
    @RequirePermission("system:operlog:query")
    public Result<SysOperLog> getOperLog(@PathVariable Long operId) {
        SysOperLog operLog = operLogService.selectOperLogById(operId);
        return Result.ok(operLog);
    }

    /**
     * 新增操作日志
     */
    @PostMapping
    public Result<Void> add(@RequestBody OperLogDTO operLogDTO) {
        operLogService.insertOperLog(operLogDTO);
        return Result.ok();
    }

    /**
     * 删除操作日志
     */
    @DeleteMapping("/{operId}")
    @RequirePermission("system:operlog:remove")
    @Log(title = "操作日志", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long operId) {
        operLogService.deleteOperLog(operId);
        return Result.ok();
    }

    /**
     * 批量删除操作日志
     */
    @DeleteMapping("/batch/{operIds}")
    @RequirePermission("system:operlog:remove")
    @Log(title = "操作日志", businessType = Log.BusinessType.DELETE)
    public Result<Void> removeBatch(@PathVariable Long[] operIds) {
        operLogService.deleteOperLogByIds(operIds);
        return Result.ok();
    }

    /**
     * 清空操作日志
     */
    @DeleteMapping("/clean")
    @RequirePermission("system:operlog:remove")
    @Log(title = "操作日志", businessType = Log.BusinessType.CLEAN)
    public Result<Void> clean() {
        operLogService.cleanOperLog();
        return Result.ok();
    }
}
