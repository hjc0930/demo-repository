package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮箱账号表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_mail_account")
public class SysMailAccount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String mail;
    private String username;
    private String password;
    private String host;
    private Integer port;
    private Boolean sslEnable;
    private String status;
    private String delFlag;
}
