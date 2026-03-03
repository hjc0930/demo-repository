package com.soybean.admin.mail.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 邮件发送DTO
 */
@Data
public class MailSendDTO {

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 收件人地址（多个用逗号分隔）
     */
    private String toAddress;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 是否HTML格式
     */
    private Boolean isHtml;

    /**
     * 模板变量
     */
    private Map<String, Object> variables;

    /**
     * 附件路径列表
     */
    private List<String> attachments;
}
