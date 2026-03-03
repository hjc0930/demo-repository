package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信发送日志表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_sms_log")
public class SysSmsLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long channelId;
    private String channelCode;
    private Long templateId;
    private String templateCode;
    private String mobile;
    private String content;
    private String params;
    private Integer sendStatus;
    private String errorMsg;
}
