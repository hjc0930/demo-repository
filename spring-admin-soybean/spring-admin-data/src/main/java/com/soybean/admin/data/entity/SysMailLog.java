package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件发送日志表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_mail_log")
public class SysMailLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Integer userType;
    private String toMail;
    private Long accountId;
    private String fromMail;
    private Long templateId;
    private String templateCode;
    private String templateTitle;
    private String templateContent;
    private String templateParams;
    private Integer sendStatus;
    private String errorMsg;
}
