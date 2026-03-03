package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysMailLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件发送日志表 Mapper接口
 */
@Mapper
public interface SysMailLogMapper extends BaseMapper<SysMailLog> {
}
