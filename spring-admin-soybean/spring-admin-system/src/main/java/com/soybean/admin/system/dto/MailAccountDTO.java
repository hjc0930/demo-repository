package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮箱账号DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MailAccountDTO extends QueryDTO {

    /** 账号ID */
    private Long id;

    /** 邮箱地址 */
    private String mail;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** SMTP主机 */
    private String host;

    /** SMTP端口 */
    private Integer port;

    /** 是否启用SSL */
    private Boolean sslEnable;

    /** 状态 */
    private String status;
}
