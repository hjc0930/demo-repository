package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件模板表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_mail_template")
public class SysMailTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;
    private String code;
    private Long accountId;
    private String nickname;
    private String title;
    private String content;
    private String params;
    private String status;
    private String delFlag;
}
