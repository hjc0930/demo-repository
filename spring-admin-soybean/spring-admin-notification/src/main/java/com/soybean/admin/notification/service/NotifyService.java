package com.soybean.admin.notification.service;

import com.soybean.admin.notification.dto.NotifyMessageDTO;
import com.soybean.admin.notification.dto.NotifySendDTO;

import java.util.List;

/**
 * 通知服务接口
 */
public interface NotifyService {

    /**
     * 发送通知
     */
    void sendNotify(NotifySendDTO notifySendDTO);

    /**
     * 使用模板发送通知
     */
    void sendTemplateNotify(Long templateId, Long userId, String title, String content);

    /**
     * 批量发送通知
     */
    void sendBatchNotify(Long templateId, List<Long> userIds, String title, String content);

    /**
     * 广播通知（所有在线用户）
     */
    void broadcastNotify(String title, String content);

    /**
     * 标记通知为已读
     */
    void markAsRead(Long messageId);

    /**
     * 批量标记通知为已读
     */
    void markAllAsRead(Long userId);

    /**
     * 删除通知
     */
    void deleteNotify(Long messageId);

    /**
     * 获取用户未读通知数量
     */
    int getUnreadCount(Long userId);

    /**
     * 推送通知到WebSocket
     */
    void pushToWebSocket(Long userId, NotifyMessageDTO message);
}
