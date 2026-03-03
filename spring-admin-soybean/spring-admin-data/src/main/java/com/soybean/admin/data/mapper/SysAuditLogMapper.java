package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysAuditLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审计日志表 Mapper接口
 */
@Mapper
public interface SysAuditLogMapper extends BaseMapper<SysAuditLog> {
}
