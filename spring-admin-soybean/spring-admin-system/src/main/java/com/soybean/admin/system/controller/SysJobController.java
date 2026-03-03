package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysJob;
import com.soybean.admin.system.dto.JobDTO;
import com.soybean.admin.system.service.SysJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务控制器
 */
@RestController
@RequestMapping("/api/system/job")
@RequiredArgsConstructor
public class SysJobController {

    private final SysJobService jobService;

    /**
     * 分页查询定时任务
     */
    @GetMapping("/page")
    @RequirePermission("system:job:list")
    public Result<IPage<SysJob>> page(Page<SysJob> page, JobDTO query) {
        IPage<SysJob> result = jobService.selectJobPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询定时任务列表
     */
    @GetMapping("/list")
    @RequirePermission("system:job:list")
    public Result<List<SysJob>> list(JobDTO query) {
        List<SysJob> list = jobService.selectJobList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询定时任务
     */
    @GetMapping("/{jobId}")
    @RequirePermission("system:job:query")
    public Result<SysJob> getJob(@PathVariable Long jobId) {
        SysJob job = jobService.selectJobById(jobId);
        return Result.ok(job);
    }

    /**
     * 新增定时任务
     */
    @PostMapping
    @RequirePermission("system:job:add")
    @Log(title = "定时任务", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody JobDTO jobDTO) {
        jobService.insertJob(jobDTO);
        return Result.ok();
    }

    /**
     * 修改定时任务
     */
    @PutMapping
    @RequirePermission("system:job:edit")
    @Log(title = "定时任务", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody JobDTO jobDTO) {
        jobService.updateJob(jobDTO);
        return Result.ok();
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping("/{jobId}")
    @RequirePermission("system:job:remove")
    @Log(title = "定时任务", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return Result.ok();
    }

    /**
     * 启动任务
     */
    @PutMapping("/start/{jobId}")
    @RequirePermission("system:job:edit")
    @Log(title = "定时任务", businessType = Log.BusinessType.UPDATE)
    public Result<Void> start(@PathVariable Long jobId) {
        jobService.startJob(jobId);
        return Result.ok();
    }

    /**
     * 暂停任务
     */
    @PutMapping("/stop/{jobId}")
    @RequirePermission("system:job:edit")
    @Log(title = "定时任务", businessType = Log.BusinessType.UPDATE)
    public Result<Void> stop(@PathVariable Long jobId) {
        jobService.stopJob(jobId);
        return Result.ok();
    }

    /**
     * 立即执行任务
     */
    @PutMapping("/execute/{jobId}")
    @RequirePermission("system:job:edit")
    @Log(title = "定时任务", businessType = Log.BusinessType.UPDATE)
    public Result<Void> execute(@PathVariable Long jobId) {
        jobService.executeJob(jobId);
        return Result.ok();
    }
}
