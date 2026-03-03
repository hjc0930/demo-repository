package com.hjc.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章查询DTO
 */
@Data
@Schema(description = "文章查询请求")
public class ArticleQueryDto {

    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", defaultValue = "10")
    private Integer pageSize = 10;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "标签ID")
    private Long tagId;

    @Schema(description = "作者ID")
    private Long authorId;

    @Schema(description = "关键词（标题/摘要）")
    private String keyword;

    @Schema(description = "状态：0-草稿，1-已发布，2-已下架")
    private Integer status;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "是否精选")
    private Integer isFeatured;

    @Schema(description = "排序字段", defaultValue = "createTime")
    private String orderBy = "createTime";

    @Schema(description = "是否升序", defaultValue = "false")
    private Boolean asc = false;
}
