package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信模板DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsTemplateDTO extends QueryDTO {

    /** 模板ID */
    private Long id;

    /** 渠道ID */
    private Long channelId;

    /** 模板编码 */
    private String code;

    /** 模板名称 */
    private String name;

    /** 模板内容 */
    private String content;

    /** 参数列表 */
    private String params;

    /** 第三方模板ID */
    private String apiTemplateId;

    /** 模板类型（1验证码 2通知 3营销） */
    private Integer type;

    /** 状态 */
    private String status;
}
