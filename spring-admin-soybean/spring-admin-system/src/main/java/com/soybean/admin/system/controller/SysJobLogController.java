package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysJobLog;
import com.soybean.admin.system.dto.JobLogDTO;
import com.soybean.admin.system.service.SysJobLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务日志控制器
 */
@RestController
@RequestMapping("/api/system/job/log")
@RequiredArgsConstructor
public class SysJobLogController {

    private final SysJobLogService jobLogService;

    /**
     * 分页查询定时任务日志
     */
    @GetMapping("/page")
    @RequirePermission("system:job:list")
    public Result<IPage<SysJobLog>> page(Page<SysJobLog> page, JobLogDTO query) {
        IPage<SysJobLog> result = jobLogService.selectJobLogPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询定时任务日志列表
     */
    @GetMapping("/list")
    @RequirePermission("system:job:list")
    public Result<List<SysJobLog>> list(JobLogDTO query) {
        List<SysJobLog> list = jobLogService.selectJobLogList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询定时任务日志
     */
    @GetMapping("/{jobLogId}")
    @RequirePermission("system:job:query")
    public Result<SysJobLog> getJobLog(@PathVariable Long jobLogId) {
        SysJobLog jobLog = jobLogService.selectJobLogById(jobLogId);
        return Result.ok(jobLog);
    }

    /**
     * 新增定时任务日志
     */
    @PostMapping
    public Result<Void> add(@RequestBody JobLogDTO jobLogDTO) {
        jobLogService.insertJobLog(jobLogDTO);
        return Result.ok();
    }

    /**
     * 删除定时任务日志
     */
    @DeleteMapping("/{jobLogId}")
    @RequirePermission("system:job:remove")
    @Log(title = "定时任务日志", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long jobLogId) {
        jobLogService.deleteJobLog(jobLogId);
        return Result.ok();
    }

    /**
     * 批量删除定时任务日志
     */
    @DeleteMapping("/batch/{jobLogIds}")
    @RequirePermission("system:job:remove")
    @Log(title = "定时任务日志", businessType = Log.BusinessType.DELETE)
    public Result<Void> removeBatch(@PathVariable Long[] jobLogIds) {
        jobLogService.deleteJobLogByIds(jobLogIds);
        return Result.ok();
    }

    /**
     * 清空定时任务日志
     */
    @DeleteMapping("/clean")
    @RequirePermission("system:job:remove")
    @Log(title = "定时任务日志", businessType = Log.BusinessType.CLEAN)
    public Result<Void> clean() {
        jobLogService.cleanJobLog();
        return Result.ok();
    }
}
