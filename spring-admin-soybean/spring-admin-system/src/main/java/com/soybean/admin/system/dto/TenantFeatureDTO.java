package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户功能开关DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantFeatureDTO extends QueryDTO {

    /** 功能ID */
    private Long id;

    /** 租户ID */
    private String tenantId;

    /** 功能键 */
    private String featureKey;

    /** 是否启用 */
    private Boolean enabled;

    /** 功能配置 */
    private String config;
}
