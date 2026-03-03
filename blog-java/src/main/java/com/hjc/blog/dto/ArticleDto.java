package com.hjc.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 文章DTO
 */
@Data
@Schema(description = "文章请求")
public class ArticleDto {

    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文章标题不能为空")
    private String title;

    @Schema(description = "文章摘要")
    private String summary;

    @Schema(description = "封面图片URL")
    private String coverImage;

    @Schema(description = "文章内容（Markdown）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文章内容不能为空")
    private String content;

    @Schema(description = "渲染后的HTML内容")
    private String contentHtml;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分类不能为空")
    private Long categoryId;

    @Schema(description = "标签ID列表")
    private List<Long> tagIds;

    @Schema(description = "是否原创：0-转载，1-原创", defaultValue = "1")
    private Integer isOriginal = 1;

    @Schema(description = "转载来源URL")
    private String sourceUrl;

    @Schema(description = "状态：0-草稿，1-已发布", defaultValue = "0")
    private Integer status = 0;
}
