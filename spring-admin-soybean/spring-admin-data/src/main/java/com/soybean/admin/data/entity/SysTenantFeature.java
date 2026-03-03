package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户功能开关表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant_feature")
public class SysTenantFeature extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String tenantId;
    private String featureKey;

    // Alias for featureKey - used by services
    @TableField(exist = false)
    private String featureCode;

    // Additional fields for service compatibility
    @TableField(exist = false)
    private String featureName;

    // Use Boolean for the field, but handle String conversion for database storage
    private Boolean enabled;
    private String config;

    /**
     * Get feature code (alias for featureKey)
     */
    public String getFeatureCode() {
        return featureKey;
    }

    /**
     * Set feature code (alias for featureKey)
     */
    public void setFeatureCode(String featureCode) {
        this.featureKey = featureCode;
        this.featureCode = featureCode;
    }

    /**
     * Get feature name (returns featureKey if not set)
     */
    public String getFeatureName() {
        return featureName != null ? featureName : featureKey;
    }

    /**
     * Get enabled as String ("1" = true, "0" = false)
     * For compatibility with code expecting String values
     */
    public String getEnabledAsString() {
        return Boolean.TRUE.equals(enabled) ? "1" : "0";
    }

    /**
     * Set enabled from String value ("1" = true, "0" = false)
     * Uses a different method name to avoid conflict with Lombok's setEnabled(Boolean)
     */
    public void setEnabledFromString(String enabledStr) {
        this.enabled = "1".equals(enabledStr) || Boolean.TRUE.toString().equalsIgnoreCase(enabledStr);
    }
}
