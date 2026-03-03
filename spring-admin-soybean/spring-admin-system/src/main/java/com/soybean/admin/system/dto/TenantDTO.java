package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 租户DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantDTO extends QueryDTO {

    /** 主键ID */
    private Long id;

    /** 租户ID */
    private String tenantId;

    /** 联系人姓名 */
    private String contactUserName;

    /** 联系电话 */
    private String contactPhone;

    /** 企业名称 */
    private String companyName;

    /** 统一社会信用代码 */
    private String licenseNumber;

    /** 地址 */
    private String address;

    /** 企业简介 */
    private String intro;

    /** 域名 */
    private String domain;

    /** 套餐ID */
    private Integer packageId;

    /** 过期时间 */
    private LocalDateTime expireTime;

    /** 用户数量（-1不限制） */
    private Integer accountCount;

    /** 存储配额（MB） */
    private Integer storageQuota;

    /** 已使用存储（MB） */
    private Integer storageUsed;

    /** API日调用量配额（-1不限制） */
    private Integer apiQuota;

    /** 状态（0正常 1停用） */
    private String status;
}
