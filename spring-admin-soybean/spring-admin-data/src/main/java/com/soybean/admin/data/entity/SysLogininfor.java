package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录日志表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_logininfor")
public class SysLogininfor extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "info_id", type = IdType.AUTO)
    private Long infoId;

    private String tenantId;
    private String userName;
    private String ipaddr;
    private String loginLocation;
    private String browser;
    private String os;
    private String deviceType;
    private String status;
    private String msg;
    private String delFlag;
    private String loginTime;
}
