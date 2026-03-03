package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysRole;
import com.soybean.admin.system.dto.RoleDTO;
import com.soybean.admin.system.dto.TreeSelectDTO;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     */
    IPage<SysRole> selectRolePage(Page<SysRole> page, RoleDTO query);

    /**
     * 查询所有角色
     */
    List<SysRole> selectRolesAll();

    /**
     * 根据角色ID查询角色
     */
    SysRole selectRoleById(Long roleId);

    /**
     * 新增角色
     */
    boolean insertRole(RoleDTO roleDTO);

    /**
     * 修改角色
     */
    boolean updateRole(RoleDTO roleDTO);

    /**
     * 删除角色
     */
    boolean deleteRole(Long roleId);

    /**
     * 构建角色树
     */
    List<TreeSelectDTO> buildRoleTree(List<SysRole> roles);
}
