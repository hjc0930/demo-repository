package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysSmsTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信模板表 Mapper接口
 */
@Mapper
public interface SysSmsTemplateMapper extends BaseMapper<SysSmsTemplate> {
}
