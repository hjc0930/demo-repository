package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站内信消息表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notify_message")
public class SysNotifyMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String tenantId;
    private Long userId;
    private Integer userType;
    private Long templateId;
    private String templateCode;
    private String templateNickname;
    private String templateContent;
    private String templateParams;
    private Boolean readStatus;
    private String delFlag;
}
