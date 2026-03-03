package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务表 Mapper接口
 */
@Mapper
public interface SysJobMapper extends BaseMapper<SysJob> {
}
