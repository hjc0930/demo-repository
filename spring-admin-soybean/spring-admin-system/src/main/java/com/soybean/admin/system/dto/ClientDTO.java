package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户端DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientDTO extends QueryDTO {

    /** 客户端ID */
    private Long id;

    /** 客户端ID */
    private String clientId;

    /** 客户端Key */
    private String clientKey;

    /** 客户端秘钥 */
    private String clientSecret;

    /** 授权类型列表 */
    private String grantTypeList;

    /** 设备类型 */
    private String deviceType;

    /** 活跃超时时间（秒） */
    private Integer activeTimeout;

    /** 超时时间（秒） */
    private Integer timeout;

    /** 状态（0正常 1停用） */
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;
}
