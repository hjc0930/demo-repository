package com.hjc.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjc.blog.entity.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 收藏Mapper接口
 */
@Mapper
public interface CollectMapper extends BaseMapper<Collect> {

    /**
     * 查询用户是否收藏了某篇文章
     *
     * @param userId    用户ID
     * @param articleId 文章ID
     * @return 收藏记录，不存在返回null
     */
    Collect selectByUserAndArticle(
            @Param("userId") Long userId,
            @Param("articleId") Long articleId
    );

    /**
     * 统计文章的收藏数
     *
     * @param articleId 文章ID
     * @return 收藏数
     */
    Integer countByArticle(@Param("articleId") Long articleId);
}
