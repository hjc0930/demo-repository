package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 租户套餐表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant_package")
public class SysTenantPackage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "package_id", type = IdType.AUTO)
    private Integer packageId;

    private String packageName;
    private String menuIds;
    private Boolean menuCheckStrictly;
    private String status;
    private String delFlag;

    // Additional fields for service compatibility
    @TableField(exist = false)
    private String description;
    @TableField(exist = false)
    private Integer maxUsers;
    @TableField(exist = false)
    private Long maxStorage;
    @TableField(exist = false)
    private String features;
    @TableField(exist = false)
    private Double price;

    /**
     * Get features list
     */
    public List<String> getFeaturesList() {
        if (features == null || features.isEmpty()) {
            return List.of();
        }
        return List.of(features.split(","));
    }

    /**
     * Get max users
     */
    public Integer getMaxUsers() {
        return maxUsers != null ? maxUsers : 100;
    }

    /**
     * Get max storage
     */
    public Long getMaxStorage() {
        return maxStorage != null ? maxStorage : 1073741824L; // 1GB default
    }

    /**
     * Get price
     */
    public Double getPrice() {
        return price != null ? price : 0.0;
    }
}
