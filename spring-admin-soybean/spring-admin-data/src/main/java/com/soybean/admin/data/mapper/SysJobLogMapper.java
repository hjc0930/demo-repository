package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志表 Mapper接口
 */
@Mapper
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {
}
