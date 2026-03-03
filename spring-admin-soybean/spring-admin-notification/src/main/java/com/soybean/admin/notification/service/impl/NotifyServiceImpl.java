package com.soybean.admin.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.soybean.admin.data.entity.SysNotifyMessage;
import com.soybean.admin.data.entity.SysNotifyTemplate;
import com.soybean.admin.data.mapper.SysNotifyMessageMapper;
import com.soybean.admin.data.mapper.SysNotifyTemplateMapper;
import com.soybean.admin.notification.dto.NotifyMessageDTO;
import com.soybean.admin.notification.dto.NotifySendDTO;
import com.soybean.admin.notification.handler.WebSocketHandler;
import com.soybean.admin.notification.service.NotifyService;
import com.soybean.admin.tenant.context.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {

    private final SysNotifyMessageMapper notifyMessageMapper;
    private final SysNotifyTemplateMapper notifyTemplateMapper;
    private final WebSocketHandler webSocketHandler;

    @Override
    public void sendNotify(NotifySendDTO notifySendDTO) {
        SysNotifyMessage message = new SysNotifyMessage();
        message.setUserId(notifySendDTO.getUserId());
        message.setTitle(notifySendDTO.getTitle());
        message.setContent(notifySendDTO.getContent());
        message.setType(notifySendDTO.getType());
        message.setIsRead(false);
        message.setSendTime(LocalDateTime.now());
        message.setTenantId(TenantContext.getTenantId());

        notifyMessageMapper.insert(message);

        // 推送到WebSocket
        NotifyMessageDTO dto = new NotifyMessageDTO();
        dto.setMessageId(message.getMessageId());
        dto.setTitle(message.getTitle());
        dto.setContent(message.getContent());
        dto.setType(message.getType());
        dto.setSendTime(message.getSendTime());

        pushToWebSocket(notifySendDTO.getUserId(), dto);
    }

    @Override
    public void sendTemplateNotify(Long templateId, Long userId, String title, String content) {
        SysNotifyTemplate template = notifyTemplateMapper.selectById(templateId);
        if (template == null) {
            throw new RuntimeException("通知模板不存在");
        }

        NotifySendDTO notifySendDTO = new NotifySendDTO();
        notifySendDTO.setUserId(userId);
        notifySendDTO.setTitle(title != null ? title : template.getTemplateTitle());
        notifySendDTO.setContent(content != null ? content : template.getTemplateContent());
        notifySendDTO.setType(template.getTemplateType());

        sendNotify(notifySendDTO);
    }

    @Override
    public void sendBatchNotify(Long templateId, List<Long> userIds, String title, String content) {
        for (Long userId : userIds) {
            try {
                sendTemplateNotify(templateId, userId, title, content);
            } catch (Exception e) {
                log.error("Failed to send notify to user: {}", userId, e);
            }
        }
    }

    @Override
    public void broadcastNotify(String title, String content) {
        NotifyMessageDTO message = new NotifyMessageDTO();
        message.setTitle(title);
        message.setContent(content);
        message.setType("broadcast");
        message.setSendTime(LocalDateTime.now());

        webSocketHandler.broadcastMessage(message);
        log.info("Broadcast notification sent: title={}", title);
    }

    @Override
    public void markAsRead(Long messageId) {
        notifyMessageMapper.update(null,
            new LambdaUpdateWrapper<SysNotifyMessage>()
                .eq(SysNotifyMessage::getMessageId, messageId)
                .set(SysNotifyMessage::getIsRead, true)
                .set(SysNotifyMessage::getReadTime, LocalDateTime.now())
        );
    }

    @Override
    public void markAllAsRead(Long userId) {
        notifyMessageMapper.update(null,
            new LambdaUpdateWrapper<SysNotifyMessage>()
                .eq(SysNotifyMessage::getUserId, userId)
                .eq(SysNotifyMessage::getIsRead, false)
                .set(SysNotifyMessage::getIsRead, true)
                .set(SysNotifyMessage::getReadTime, LocalDateTime.now())
        );
    }

    @Override
    public void deleteNotify(Long messageId) {
        notifyMessageMapper.deleteById(messageId);
    }

    @Override
    public int getUnreadCount(Long userId) {
        return Math.toIntExact(notifyMessageMapper.selectCount(
            new LambdaQueryWrapper<SysNotifyMessage>()
                .eq(SysNotifyMessage::getUserId, userId)
                .eq(SysNotifyMessage::getIsRead, false)
        ));
    }

    @Override
    public void pushToWebSocket(Long userId, NotifyMessageDTO message) {
        if (webSocketHandler.isUserOnline(String.valueOf(userId))) {
            webSocketHandler.sendMessageToUser(String.valueOf(userId), message);
            log.debug("Notification pushed to WebSocket: userId={}, title={}", userId, message.getTitle());
        } else {
            log.debug("User offline, notification saved: userId={}", userId);
        }
    }
}
