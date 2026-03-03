package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysTenantFeature;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户功能开关表 Mapper接口
 */
@Mapper
public interface SysTenantFeatureMapper extends BaseMapper<SysTenantFeature> {
}
