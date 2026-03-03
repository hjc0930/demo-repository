package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信日志DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsLogDTO extends QueryDTO {

    /** 日志ID */
    private Long id;

    /** 渠道ID */
    private Long channelId;

    /** 渠道编码 */
    private String channelCode;

    /** 模板ID */
    private Long templateId;

    /** 模板编码 */
    private String templateCode;

    /** 手机号 */
    private String mobile;

    /** 短信内容 */
    private String content;

    /** 参数 */
    private String params;

    /** 发送状态（0发送中 1成功 2失败） */
    private Integer sendStatus;

    /** 错误信息 */
    private String errorMsg;
}
