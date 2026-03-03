package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件日志DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MailLogDTO extends QueryDTO {

    /** 日志ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 用户类型 */
    private Integer userType;

    /** 收件邮箱 */
    private String toMail;

    /** 发送账号ID */
    private Long accountId;

    /** 发件邮箱 */
    private String fromMail;

    /** 模板ID */
    private Long templateId;

    /** 模板编码 */
    private String templateCode;

    /** 模板标题 */
    private String templateTitle;

    /** 模板内容 */
    private String templateContent;

    /** 模板参数 */
    private String templateParams;

    /** 发送状态（0发送中 1成功 2失败） */
    private Integer sendStatus;

    /** 错误信息 */
    private String errorMsg;
}
