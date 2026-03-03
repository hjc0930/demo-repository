package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件模板DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MailTemplateDTO extends QueryDTO {

    /** 模板ID */
    private Long id;

    /** 模板名称 */
    private String name;

    /** 模板编码 */
    private String code;

    /** 发送账号ID */
    private Long accountId;

    /** 发送人昵称 */
    private String nickname;

    /** 邮件标题 */
    private String title;

    /** 邮件内容 */
    private String content;

    /** 参数列表 */
    private String params;

    /** 状态 */
    private String status;
}
