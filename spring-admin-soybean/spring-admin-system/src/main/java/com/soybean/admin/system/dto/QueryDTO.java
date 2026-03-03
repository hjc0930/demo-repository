package com.soybean.admin.system.dto;

import lombok.Data;

/**
 * 查询DTO
 */
@Data
public class QueryDTO {
    /** 关键字搜索 */
    private String keyword;
    /** 状态 */
    private String status;
    /** 时间范围参数 */
    private String params;
    /** 页码 */
    private Integer pageNum;
    /** 页面大小 */
    private Integer pageSize;
}
