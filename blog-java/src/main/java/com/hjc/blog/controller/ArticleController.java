package com.hjc.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjc.blog.common.result.Result;
import com.hjc.blog.common.utils.SecurityContextUtil;
import com.hjc.blog.dto.ArticleQueryDto;
import com.hjc.blog.dto.ArticleDto;
import com.hjc.blog.service.ArticleService;
import com.hjc.blog.vo.ArticleListVo;
import com.hjc.blog.vo.ArticleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 文章控制器
 */
@Tag(name = "文章管理", description = "文章的增删改查、发布/下架、置顶/精选等接口")
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 发布文章
     */
    @Operation(summary = "发布文章", description = "创建新文章，可以保存为草稿或直接发布")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Result<Long> publishArticle(@Valid @RequestBody ArticleDto request) {
        Long userId = SecurityContextUtil.getRequiredUserId();
        Long articleId = articleService.publishArticle(request, userId);
        return Result.success(articleId);
    }

    /**
     * 更新文章
     */
    @Operation(summary = "更新文章", description = "更新文章内容，只有作者或管理员可以操作")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Result<Void> updateArticle(
            @Parameter(description = "文章ID") @PathVariable Long id,
            @Valid @RequestBody ArticleDto request) {
        Long userId = SecurityContextUtil.getRequiredUserId();
        boolean isAdmin = SecurityContextUtil.isAdmin();
        articleService.updateArticle(id, request, userId, isAdmin);
        return Result.success();
    }

    /**
     * 删除文章
     */
    @Operation(summary = "删除文章", description = "删除文章，只有作者或管理员可以操作")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Result<Void> deleteArticle(
            @Parameter(description = "文章ID") @PathVariable Long id) {
        Long userId = SecurityContextUtil.getRequiredUserId();
        boolean isAdmin = SecurityContextUtil.isAdmin();
        articleService.deleteArticle(id, userId, isAdmin);
        return Result.success();
    }

    /**
     * 获取文章详情
     */
    @Operation(summary = "获取文章详情", description = "获取文章完整内容，会增加浏览量")
    @GetMapping("/{id}")
    public Result<ArticleVo> getArticleDetail(
            @Parameter(description = "文章ID") @PathVariable Long id) {
        ArticleVo article = articleService.getArticleDetail(id);
        return Result.success(article);
    }

    /**
     * 分页查询文章列表
     */
    @Operation(summary = "分页查询文章", description = "支持按分类、标签、作者、关键词等条件查询")
    @GetMapping("/list")
    public Result<Page<ArticleListVo>> listArticles(ArticleQueryDto request) {
        Page<ArticleListVo> page = articleService.listArticles(request);
        return Result.success(page);
    }

    /**
     * 发布草稿
     */
    @Operation(summary = "发布草稿", description = "将草稿状态的文章发布，只有作者或管理员可以操作")
    @PutMapping("/{id}/publish")
    @PreAuthorize("isAuthenticated()")
    public Result<Void> publishDraft(
            @Parameter(description = "文章ID") @PathVariable Long id) {
        Long userId = SecurityContextUtil.getRequiredUserId();
        boolean isAdmin = SecurityContextUtil.isAdmin();
        articleService.publishDraft(id, userId, isAdmin);
        return Result.success();
    }

    /**
     * 下架文章
     */
    @Operation(summary = "下架文章", description = "将已发布的文章下架，只有作者或管理员可以操作")
    @PutMapping("/{id}/offline")
    @PreAuthorize("isAuthenticated()")
    public Result<Void> offlineArticle(
            @Parameter(description = "文章ID") @PathVariable Long id) {
        Long userId = SecurityContextUtil.getRequiredUserId();
        boolean isAdmin = SecurityContextUtil.isAdmin();
        articleService.offlineArticle(id, userId, isAdmin);
        return Result.success();
    }

    /**
     * 设置置顶
     */
    @Operation(summary = "设置置顶", description = "设置文章是否置顶，仅管理员可用")
    @PutMapping("/{id}/top")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Void> setTop(
            @Parameter(description = "文章ID") @PathVariable Long id,
            @Parameter(description = "是否置顶：0-否，1-是") @RequestParam Integer isTop) {
        articleService.setTop(id, isTop);
        return Result.success();
    }

    /**
     * 设置精选
     */
    @Operation(summary = "设置精选", description = "设置文章是否精选，仅管理员可用")
    @PutMapping("/{id}/featured")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Void> setFeatured(
            @Parameter(description = "文章ID") @PathVariable Long id,
            @Parameter(description = "是否精选：0-否，1-是") @RequestParam Integer isFeatured) {
        articleService.setFeatured(id, isFeatured);
        return Result.success();
    }
}
