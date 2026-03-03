package com.hjc.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjc.blog.dto.ArticleQueryDto;
import com.hjc.blog.dto.ArticleDto;
import com.hjc.blog.entity.Article;
import com.hjc.blog.vo.ArticleListVo;
import com.hjc.blog.vo.ArticleVo;

/**
 * 文章服务接口
 */
public interface ArticleService extends IService<Article> {

    /**
     * 发布文章
     *
     * @param request 文章请求
     * @param authorId 作者ID
     * @return 文章ID
     */
    Long publishArticle(ArticleDto request, Long authorId);

    /**
     * 更新文章
     *
     * @param id      文章ID
     * @param request 文章请求
     * @param userId  当前用户ID
     * @param isAdmin 是否为管理员
     */
    void updateArticle(Long id, ArticleDto request, Long userId, boolean isAdmin);

    /**
     * 删除文章
     *
     * @param id      文章ID
     * @param userId  当前用户ID
     * @param isAdmin 是否为管理员
     */
    void deleteArticle(Long id, Long userId, boolean isAdmin);

    /**
     * 获取文章详情
     *
     * @param id 文章ID
     * @return 文章详情
     */
    ArticleVo getArticleDetail(Long id);

    /**
     * 分页查询文章列表
     *
     * @param request 查询请求
     * @return 分页结果
     */
    Page<ArticleListVo> listArticles(ArticleQueryDto request);

    /**
     * 发布草稿
     *
     * @param id      文章ID
     * @param userId  当前用户ID
     * @param isAdmin 是否为管理员
     */
    void publishDraft(Long id, Long userId, boolean isAdmin);

    /**
     * 下架文章
     *
     * @param id      文章ID
     * @param userId  当前用户ID
     * @param isAdmin 是否为管理员
     */
    void offlineArticle(Long id, Long userId, boolean isAdmin);

    /**
     * 设置置顶
     *
     * @param id     文章ID
     * @param isTop  是否置顶
     */
    void setTop(Long id, Integer isTop);

    /**
     * 设置精选
     *
     * @param id        文章ID
     * @param isFeatured 是否精选
     */
    void setFeatured(Long id, Integer isFeatured);
}
