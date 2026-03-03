package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户表 Mapper接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户ID查询用户角色列表
     */
    @Select("SELECT r.role_id, r.role_name, r.role_key, r.role_sort, r.data_scope, " +
            "r.menu_check_strictly, r.dept_check_strictly, r.status " +
            "FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.del_flag = '0' AND r.status = '0'")
    List<String> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询用户权限列表
     */
    @Select("SELECT DISTINCT m.perms " +
            "FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.menu_id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.status = '0' AND m.perms IS NOT NULL AND m.perms != ''")
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 根据租户ID和用户名查询用户
     */
    @Select("SELECT * FROM sys_user WHERE tenant_id = #{tenantId} AND user_name = #{userName} AND del_flag = '0'")
    SysUser selectByTenantIdAndUserName(@Param("tenantId") String tenantId, @Param("userName") String userName);
}
