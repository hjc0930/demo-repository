package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysTenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 租户表 Mapper接口
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {

    /**
     * 根据租户ID查询租户
     */
    @Select("SELECT * FROM sys_tenant WHERE tenant_id = #{tenantId} AND del_flag = '0'")
    SysTenant selectByTenantId(@Param("tenantId") String tenantId);

    /**
     * 检查租户是否存在
     */
    @Select("SELECT COUNT(1) FROM sys_tenant WHERE tenant_id = #{tenantId} AND status = '0' AND del_flag = '0'")
    int checkTenantExists(@Param("tenantId") String tenantId);
}
