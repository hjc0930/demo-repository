package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站内信模板表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notify_template")
public class SysNotifyTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;
    private String code;
    private String nickname;
    private String content;
    private String params;
    private Integer type;
    private String status;
    private String delFlag;
}
