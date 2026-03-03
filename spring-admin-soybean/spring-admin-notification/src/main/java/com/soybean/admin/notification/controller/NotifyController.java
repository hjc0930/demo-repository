package com.soybean.admin.notification.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.response.PageResult;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysNotifyMessage;
import com.soybean.admin.data.entity.SysNotifyTemplate;
import com.soybean.admin.data.mapper.SysNotifyMessageMapper;
import com.soybean.admin.data.mapper.SysNotifyTemplateMapper;
import com.soybean.admin.notification.dto.NotifySendDTO;
import com.soybean.admin.notification.handler.WebSocketHandler;
import com.soybean.admin.notification.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知管理控制器
 */
@RestController
@RequestMapping("/api/system/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final NotifyService notifyService;
    private final SysNotifyMessageMapper notifyMessageMapper;
    private final SysNotifyTemplateMapper notifyTemplateMapper;
    private final WebSocketHandler webSocketHandler;

    // ==================== 通知消息管理 ====================

    /**
     * 分页查询通知消息列表
     */
    @GetMapping("/message/list")
    public Result<PageResult<SysNotifyMessage>> listMessage(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SysNotifyMessage> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysNotifyMessage> wrapper = new LambdaQueryWrapper<>();

        if (userId != null) {
            wrapper.eq(SysNotifyMessage::getUserId, userId);
        }
        if (isRead != null) {
            wrapper.eq(SysNotifyMessage::getIsRead, isRead);
        }

        wrapper.orderByDesc(SysNotifyMessage::getSendTime);
        IPage<SysNotifyMessage> result = notifyMessageMapper.selectPage(page, wrapper);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取通知消息详情
     */
    @GetMapping("/message/{id}")
    public Result<SysNotifyMessage> getMessage(@PathVariable Long id) {
        return Result.success(notifyMessageMapper.selectById(id));
    }

    /**
     * 发送通知
     */
    @PostMapping("/send")
    public Result<Void> sendNotify(@RequestBody NotifySendDTO notifySendDTO) {
        notifyService.sendNotify(notifySendDTO);
        return Result.success();
    }

    /**
     * 标记通知为已读
     */
    @PutMapping("/message/{id}/read")
    public Result<Void> markAsRead(@PathVariable Long id) {
        notifyService.markAsRead(id);
        return Result.success();
    }

    /**
     * 标记所有通知为已读
     */
    @PutMapping("/message/read-all")
    public Result<Void> markAllAsRead(@RequestParam Long userId) {
        notifyService.markAllAsRead(userId);
        return Result.success();
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/message/{id}")
    public Result<Void> deleteMessage(@PathVariable Long id) {
        notifyService.deleteNotify(id);
        return Result.success();
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread-count")
    public Result<Integer> getUnreadCount(@RequestParam Long userId) {
        return Result.success(notifyService.getUnreadCount(userId));
    }

    // ==================== 通知模板管理 ====================

    /**
     * 分页查询通知模板列表
     */
    @GetMapping("/template/list")
    public Result<PageResult<SysNotifyTemplate>> listTemplate(
            @RequestParam(required = false) String templateName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SysNotifyTemplate> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysNotifyTemplate> wrapper = new LambdaQueryWrapper<>();

        if (templateName != null) {
            wrapper.like(SysNotifyTemplate::getTemplateName, templateName);
        }

        wrapper.orderByDesc(SysNotifyTemplate::getCreateTime);
        IPage<SysNotifyTemplate> result = notifyTemplateMapper.selectPage(page, wrapper);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取通知模板详情
     */
    @GetMapping("/template/{id}")
    public Result<SysNotifyTemplate> getTemplate(@PathVariable Long id) {
        return Result.success(notifyTemplateMapper.selectById(id));
    }

    /**
     * 新增通知模板
     */
    @PostMapping("/template")
    public Result<Void> addTemplate(@RequestBody SysNotifyTemplate template) {
        notifyTemplateMapper.insert(template);
        return Result.success();
    }

    /**
     * 修改通知模板
     */
    @PutMapping("/template")
    public Result<Void> updateTemplate(@RequestBody SysNotifyTemplate template) {
        notifyTemplateMapper.updateById(template);
        return Result.success();
    }

    /**
     * 删除通知模板
     */
    @DeleteMapping("/template/{id}")
    public Result<Void> deleteTemplate(@PathVariable Long id) {
        notifyTemplateMapper.deleteById(id);
        return Result.success();
    }

    // ==================== WebSocket状态 ====================

    /**
     * 获取在线用户数量
     */
    @GetMapping("/online-count")
    public Result<Map<String, Object>> getOnlineStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("onlineCount", webSocketHandler.getOnlineUserCount());
        return Result.success(result);
    }
}
