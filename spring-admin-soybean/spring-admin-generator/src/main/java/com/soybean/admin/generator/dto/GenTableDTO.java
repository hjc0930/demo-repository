package com.soybean.admin.generator.dto;

import lombok.Data;

/**
 * 代码生成表查询DTO
 */
@Data
public class GenTableDTO {

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableComment;

    /**
     * 关键字搜索
     */
    private String keyword;
}
