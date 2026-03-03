package com.soybean.admin.notification.dto;

import lombok.Data;

/**
 * 通知发送DTO
 */
@Data
public class NotifySendDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知类型（system-系统, message-消息, notice-公告）
     */
    private String type;
}
