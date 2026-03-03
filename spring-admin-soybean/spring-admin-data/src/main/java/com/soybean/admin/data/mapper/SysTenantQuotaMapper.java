package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysTenantQuota;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户配额表 Mapper接口
 */
@Mapper
public interface SysTenantQuotaMapper extends BaseMapper<SysTenantQuota> {
}
