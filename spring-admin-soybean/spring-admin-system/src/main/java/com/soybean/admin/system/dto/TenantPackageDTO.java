package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户套餐DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantPackageDTO extends QueryDTO {

    /** 套餐ID */
    private Integer packageId;

    /** 套餐名称 */
    private String packageName;

    /** 关联菜单ID */
    private String menuIds;

    /** 菜单树选择项是否关联显示 */
    private Boolean menuCheckStrictly;

    /** 状态 */
    private String status;
}
