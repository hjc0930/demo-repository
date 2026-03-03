package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysSmsLog;
import com.soybean.admin.system.dto.SmsLogDTO;
import com.soybean.admin.system.service.SysSmsLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短信日志控制器
 */
@RestController
@RequestMapping("/api/system/sms/log")
@RequiredArgsConstructor
public class SysSmsLogController {

    private final SysSmsLogService smsLogService;

    /**
     * 分页查询短信日志
     */
    @GetMapping("/page")
    @RequirePermission("system:sms:log:list")
    public Result<IPage<SysSmsLog>> page(Page<SysSmsLog> page, SmsLogDTO query) {
        IPage<SysSmsLog> result = smsLogService.selectSmsLogPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询短信日志列表
     */
    @GetMapping("/list")
    @RequirePermission("system:sms:log:list")
    public Result<List<SysSmsLog>> list(SmsLogDTO query) {
        List<SysSmsLog> list = smsLogService.selectSmsLogList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询短信日志
     */
    @GetMapping("/{id}")
    @RequirePermission("system:sms:log:query")
    public Result<SysSmsLog> getSmsLog(@PathVariable Long id) {
        SysSmsLog smsLog = smsLogService.selectSmsLogById(id);
        return Result.ok(smsLog);
    }

    /**
     * 批量删除短信日志
     */
    @DeleteMapping("/batch/{ids}")
    @RequirePermission("system:sms:log:remove")
    @Log(title = "短信日志", businessType = Log.BusinessType.DELETE)
    public Result<Void> removeBatch(@PathVariable Long[] ids) {
        smsLogService.deleteSmsLogByIds(ids);
        return Result.ok();
    }

    /**
     * 清空短信日志
     */
    @DeleteMapping("/clean")
    @RequirePermission("system:sms:log:remove")
    @Log(title = "短信日志", businessType = Log.BusinessType.CLEAN)
    public Result<Void> clean() {
        smsLogService.cleanSmsLog();
        return Result.ok();
    }
}
