package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户配额表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant_quota")
public class SysTenantQuota extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String tenantId;
    private Integer userQuota;
    private Integer userUsed;
    private Long storageQuota;
    private Long storageUsed;
    private Integer apiQuota;
    private Integer apiUsed;

    // Generic quota fields for service compatibility
    private Long quotaLimit;
    private Long quotaUsed;
    private String quotaType;

    /**
     * Set quota used (updates userUsed or generic quotaUsed)
     */
    public void setQuotaUsed(long used) {
        this.quotaUsed = used;
    }

    /**
     * Set quota limit (updates userQuota or generic quotaLimit)
     */
    public void setQuotaLimit(long limit) {
        this.quotaLimit = limit;
    }

    /**
     * Get quota used
     */
    public long getQuotaUsed() {
        return quotaUsed != null ? quotaUsed : (userUsed != null ? userUsed.longValue() : 0L);
    }

    /**
     * Get quota limit
     */
    public long getQuotaLimit() {
        return quotaLimit != null ? quotaLimit : (userQuota != null ? userQuota.longValue() : 0L);
    }
}
