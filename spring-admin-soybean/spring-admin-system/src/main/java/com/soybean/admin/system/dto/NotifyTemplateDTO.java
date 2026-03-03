package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站内信模板DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NotifyTemplateDTO extends QueryDTO {

    /** 模板ID */
    private Long id;

    /** 模板名称 */
    private String name;

    /** 模板编码 */
    private String code;

    /** 发送人名称 */
    private String nickname;

    /** 模板内容 */
    private String content;

    /** 参数列表 */
    private String params;

    /** 模板类型（1系统通知 2业务通知） */
    private Integer type;

    /** 状态 */
    private String status;
}
