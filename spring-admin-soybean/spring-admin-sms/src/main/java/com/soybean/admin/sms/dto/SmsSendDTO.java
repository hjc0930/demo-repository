package com.soybean.admin.sms.dto;

import lombok.Data;

import java.util.Map;

/**
 * 短信发送DTO
 */
@Data
public class SmsSendDTO {

    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 模板参数
     */
    private Map<String, String> params;
}
