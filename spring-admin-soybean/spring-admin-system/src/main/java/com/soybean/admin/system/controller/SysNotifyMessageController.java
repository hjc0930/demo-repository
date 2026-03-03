package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysNotifyMessage;
import com.soybean.admin.system.dto.NotifyMessageDTO;
import com.soybean.admin.system.service.SysNotifyMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 站内信消息控制器
 */
@RestController
@RequestMapping("/api/system/notify/message")
@RequiredArgsConstructor
public class SysNotifyMessageController {

    private final SysNotifyMessageService notifyMessageService;

    /**
     * 分页查询站内信消息
     */
    @GetMapping("/page")
    public Result<IPage<SysNotifyMessage>> page(Page<SysNotifyMessage> page, NotifyMessageDTO query) {
        IPage<SysNotifyMessage> result = notifyMessageService.selectNotifyMessagePage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询站内信消息列表
     */
    @GetMapping("/list")
    public Result<List<SysNotifyMessage>> list(NotifyMessageDTO query) {
        List<SysNotifyMessage> list = notifyMessageService.selectNotifyMessageList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询站内信消息
     */
    @GetMapping("/{id}")
    public Result<SysNotifyMessage> getNotifyMessage(@PathVariable Long id) {
        SysNotifyMessage message = notifyMessageService.selectNotifyMessageById(id);
        return Result.ok(message);
    }

    /**
     * 查询用户未读消息数量
     */
    @GetMapping("/unread/{userId}")
    public Result<Long> getUnreadCount(@PathVariable Long userId) {
        Long count = notifyMessageService.selectUnreadCount(userId);
        return Result.ok(count);
    }

    /**
     * 标记消息为已读
     */
    @PutMapping("/read/{id}")
    @Log(title = "站内信消息", businessType = Log.BusinessType.UPDATE)
    public Result<Void> markAsRead(@PathVariable Long id) {
        notifyMessageService.markAsRead(id);
        return Result.ok();
    }

    /**
     * 批量标记消息为已读
     */
    @PutMapping("/read/all/{userId}")
    @Log(title = "站内信消息", businessType = Log.BusinessType.UPDATE)
    public Result<Void> markAllAsRead(@PathVariable Long userId) {
        notifyMessageService.markAllAsRead(userId);
        return Result.ok();
    }

    /**
     * 删除站内信消息
     */
    @DeleteMapping("/{id}")
    @Log(title = "站内信消息", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long id) {
        notifyMessageService.deleteNotifyMessage(id);
        return Result.ok();
    }
}
