package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysSmsChannel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信渠道表 Mapper接口
 */
@Mapper
public interface SysSmsChannelMapper extends BaseMapper<SysSmsChannel> {
}
