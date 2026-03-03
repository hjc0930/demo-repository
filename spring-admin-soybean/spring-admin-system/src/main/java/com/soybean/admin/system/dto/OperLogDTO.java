package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OperLogDTO extends QueryDTO {

    /** 日志ID */
    private Long operId;

    /** 模块标题 */
    private String title;

    /** 业务类型（0其它 1新增 2修改 3删除） */
    private Integer businessType;

    /** 请求方式 */
    private String requestMethod;

    /** 操作类别（0其它 1后台用户 2手机端用户） */
    private Integer operatorType;

    /** 操作人员 */
    private String operName;

    /** 部门名称 */
    private String deptName;

    /** 请求URL */
    private String operUrl;

    /** 操作地点 */
    private String operLocation;

    /** 请求参数 */
    private String operParam;

    /** 返回参数 */
    private String jsonResult;

    /** 错误消息 */
    private String errorMsg;

    /** 方法名称 */
    private String method;

    /** 操作IP */
    private String operIp;

    /** 操作状态（0正常 1异常） */
    private String status;

    /** 消耗时间（毫秒） */
    private Integer costTime;

    /** 操作时间 */
    private String operTime;
}
