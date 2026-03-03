package com.hjc.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjc.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章Mapper接口
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 增加文章浏览量
     *
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementViewCount(@Param("articleId") Long articleId);

    /**
     * 增加文章点赞数
     *
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementLikeCount(@Param("articleId") Long articleId);

    /**
     * 减少文章点赞数
     *
     * @param articleId 文章ID
     * @return 影响行数
     */
    int decrementLikeCount(@Param("articleId") Long articleId);

    /**
     * 增加文章评论数
     *
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementCommentCount(@Param("articleId") Long articleId);

    /**
     * 减少文章评论数
     *
     * @param articleId 文章ID
     * @return 影响行数
     */
    int decrementCommentCount(@Param("articleId") Long articleId);

    /**
     * 增加文章收藏数
     *
     * @param articleId 文章ID
     * @return 影响行数
     */
    int incrementCollectCount(@Param("articleId") Long articleId);

    /**
     * 减少文章收藏数
     *
     * @param articleId 文章ID
     * @return 影响行数
     */
    int decrementCollectCount(@Param("articleId") Long articleId);
}
