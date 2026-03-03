package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信渠道表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_sms_channel")
public class SysSmsChannel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String code;
    private String name;
    private String signature;
    private String apiKey;
    private String apiSecret;
    private String callbackUrl;
    private String status;
    private String delFlag;
}
