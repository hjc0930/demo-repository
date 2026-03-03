package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysDept;
import com.soybean.admin.system.dto.DeptDTO;
import com.soybean.admin.system.dto.TreeSelectDTO;

import java.util.List;

/**
 * 部门服务接口
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 查询部门列表
     */
    List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据用户ID查询部门列表
     */
    List<SysDept> selectDeptsByUserId(Long userId);

    /**
     * 查询部门树
     */
    List<SysDept> selectDeptTree(SysDept dept);

    /**
     * 根据部门ID查询部门
     */
    SysDept selectDeptById(Long deptId);

    /**
     * 新增部门
     */
    boolean insertDept(DeptDTO deptDTO);

    /**
     * 修改部门
     */
    boolean updateDept(DeptDTO deptDTO);

    /**
     * 删除部门
     */
    boolean deleteDept(Long deptId);

    /**
     * 构建部门树选择
     */
    List<TreeSelectDTO> buildDeptTreeSelect(List<SysDept> depts);

    /**
     * 检查是否存在子部门
     */
    boolean hasChildByDeptId(Long deptId);
}
