package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 部门表 Mapper接口
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {

    /**
     * 根据用户ID查询部门列表
     */
    @Select("SELECT DISTINCT d.* FROM sys_dept d " +
            "LEFT JOIN sys_user_role ur ON ur.user_id = #{userId} " +
            "LEFT JOIN sys_role_dept rd ON d.dept_id = rd.dept_id AND rd.role_id = ur.role_id " +
            "WHERE d.del_flag = '0' AND d.status = '0' " +
            "ORDER BY d.parent_id, d.order_num")
    List<SysDept> selectDeptsByUserId(@Param("userId") Long userId);

    /**
     * 根据父部门ID查询子部门列表
     */
    @Select("SELECT * FROM sys_dept WHERE parent_id = #{parentId} AND del_flag = '0' ORDER BY order_num")
    List<SysDept> selectDeptsByParentId(@Param("parentId") Long parentId);
}
