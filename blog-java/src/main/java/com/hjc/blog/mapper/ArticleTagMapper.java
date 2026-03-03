package com.hjc.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjc.blog.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章标签关联Mapper接口
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    /**
     * 根据文章ID查询标签ID列表
     *
     * @param articleId 文章ID
     * @return 标签ID列表
     */
    List<Long> selectTagIdsByArticleId(@Param("articleId") Long articleId);

    /**
     * 根据文章ID删除标签关联
     *
     * @param articleId 文章ID
     * @return 影响行数
     */
    int deleteByArticleId(@Param("articleId") Long articleId);

    /**
     * 根据标签ID查询文章ID列表
     *
     * @param tagId 标签ID
     * @return 文章ID列表
     */
    List<Long> selectArticleIdsByTagId(@Param("tagId") Long tagId);
}
