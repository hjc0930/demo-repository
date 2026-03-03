package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户配额DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantQuotaDTO extends QueryDTO {

    /** 配额ID */
    private Long id;

    /** 租户ID */
    private String tenantId;

    /** 用户数量配额 */
    private Integer userQuota;

    /** 已使用用户数 */
    private Integer userUsed;

    /** 存储配额（MB） */
    private Long storageQuota;

    /** 已使用存储（MB） */
    private Long storageUsed;

    /** API调用配额 */
    private Integer apiQuota;

    /** 本月已调用次数 */
    private Integer apiUsed;
}
