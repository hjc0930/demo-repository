package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站内信消息DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NotifyMessageDTO extends QueryDTO {

    /** 消息ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 用户类型 */
    private Integer userType;

    /** 模板ID */
    private Long templateId;

    /** 模板编码 */
    private String templateCode;

    /** 模板内容 */
    private String templateContent;

    /** 模板参数 */
    private String templateParams;

    /** 阅读状态 */
    private Boolean readStatus;
}
