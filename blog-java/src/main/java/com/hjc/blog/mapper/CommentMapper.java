package com.hjc.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjc.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 评论Mapper接口
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 增加评论点赞数
     *
     * @param commentId 评论ID
     * @return 影响行数
     */
    int incrementLikeCount(@Param("commentId") Long commentId);

    /**
     * 减少评论点赞数
     *
     * @param commentId 评论ID
     * @return 影响行数
     */
    int decrementLikeCount(@Param("commentId") Long commentId);
}
