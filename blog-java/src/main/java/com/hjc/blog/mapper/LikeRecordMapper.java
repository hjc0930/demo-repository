package com.hjc.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjc.blog.entity.LikeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 点赞记录Mapper接口
 */
@Mapper
public interface LikeRecordMapper extends BaseMapper<LikeRecord> {

    /**
     * 查询用户对目标的点赞状态
     *
     * @param userId     用户ID
     * @param targetId   目标ID
     * @param targetType 目标类型：1-文章，2-评论
     * @return 点赞记录，不存在返回null
     */
    LikeRecord selectByUserAndTarget(
            @Param("userId") Long userId,
            @Param("targetId") Long targetId,
            @Param("targetType") Integer targetType
    );

    /**
     * 统计目标的点赞数
     *
     * @param targetId   目标ID
     * @param targetType 目标类型
     * @return 点赞数
     */
    Integer countByTarget(
            @Param("targetId") Long targetId,
            @Param("targetType") Integer targetType
    );
}
