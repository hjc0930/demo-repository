package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信渠道DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsChannelDTO extends QueryDTO {

    /** 渠道ID */
    private Long id;

    /** 渠道编码 */
    private String code;

    /** 渠道名称 */
    private String name;

    /** 短信签名 */
    private String signature;

    /** API Key */
    private String apiKey;

    /** API Secret */
    private String apiSecret;

    /** 回调URL */
    private String callbackUrl;

    /** 状态 */
    private String status;
}
