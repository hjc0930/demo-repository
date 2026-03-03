package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysSmsLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信发送日志表 Mapper接口
 */
@Mapper
public interface SysSmsLogMapper extends BaseMapper<SysSmsLog> {
}
