package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审计日志表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_audit_log")
public class SysAuditLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String tenantId;
    private Long userId;
    private String userName;
    private String action;
    private String module;
    private String targetType;
    private String targetId;
    private String oldValue;
    private String newValue;
    private String ip;
    private String userAgent;
    private String requestId;
    private String status;
    private String errorMsg;
    private Integer duration;
}
