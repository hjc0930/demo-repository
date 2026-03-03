package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysMailLog;
import com.soybean.admin.system.dto.MailLogDTO;
import com.soybean.admin.system.service.SysMailLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮件日志控制器
 */
@RestController
@RequestMapping("/api/system/mail/log")
@RequiredArgsConstructor
public class SysMailLogController {

    private final SysMailLogService mailLogService;

    /**
     * 分页查询邮件日志
     */
    @GetMapping("/page")
    @RequirePermission("system:mail:log:list")
    public Result<IPage<SysMailLog>> page(Page<SysMailLog> page, MailLogDTO query) {
        IPage<SysMailLog> result = mailLogService.selectMailLogPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询邮件日志列表
     */
    @GetMapping("/list")
    @RequirePermission("system:mail:log:list")
    public Result<List<SysMailLog>> list(MailLogDTO query) {
        List<SysMailLog> list = mailLogService.selectMailLogList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询邮件日志
     */
    @GetMapping("/{id}")
    @RequirePermission("system:mail:log:query")
    public Result<SysMailLog> getMailLog(@PathVariable Long id) {
        SysMailLog mailLog = mailLogService.selectMailLogById(id);
        return Result.ok(mailLog);
    }

    /**
     * 批量删除邮件日志
     */
    @DeleteMapping("/batch/{ids}")
    @RequirePermission("system:mail:log:remove")
    @Log(title = "邮件日志", businessType = Log.BusinessType.DELETE)
    public Result<Void> removeBatch(@PathVariable Long[] ids) {
        mailLogService.deleteMailLogByIds(ids);
        return Result.ok();
    }

    /**
     * 清空邮件日志
     */
    @DeleteMapping("/clean")
    @RequirePermission("system:mail:log:remove")
    @Log(title = "邮件日志", businessType = Log.BusinessType.CLEAN)
    public Result<Void> clean() {
        mailLogService.cleanMailLog();
        return Result.ok();
    }
}
