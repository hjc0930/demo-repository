package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信模板表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_sms_template")
public class SysSmsTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long channelId;
    private String code;
    private String name;
    private String content;
    private String params;
    private String apiTemplateId;
    private Integer type;
    private String status;
    private String delFlag;
}
