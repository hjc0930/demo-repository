package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysMailTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件模板表 Mapper接口
 */
@Mapper
public interface SysMailTemplateMapper extends BaseMapper<SysMailTemplate> {
}
