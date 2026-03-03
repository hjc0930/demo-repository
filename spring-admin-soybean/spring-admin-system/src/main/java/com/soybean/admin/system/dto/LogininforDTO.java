package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录日志DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogininforDTO extends QueryDTO {

    /** 日志ID */
    private Long infoId;

    /** 用户账号 */
    private String userName;

    /** 登录IP地址 */
    private String ipaddr;

    /** 登录地点 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 设备类型 */
    private String deviceType;

    /** 登录状态（0成功 1失败） */
    private String status;

    /** 提示消息 */
    private String msg;
}
