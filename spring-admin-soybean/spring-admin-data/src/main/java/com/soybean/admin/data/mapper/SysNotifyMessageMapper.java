package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysNotifyMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 站内信消息表 Mapper接口
 */
@Mapper
public interface SysNotifyMessageMapper extends BaseMapper<SysNotifyMessage> {
}
