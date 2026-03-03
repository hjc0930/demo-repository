package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysRole;
import com.soybean.admin.data.entity.SysRoleDept;
import com.soybean.admin.data.entity.SysRoleMenu;
import com.soybean.admin.data.mapper.SysRoleMapper;
import com.soybean.admin.system.dto.RoleDTO;
import com.soybean.admin.system.dto.TreeSelectDTO;
import com.soybean.admin.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper roleMapper;

    @Override
    public IPage<SysRole> selectRolePage(Page<SysRole> page, RoleDTO query) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getRoleName())) {
            wrapper.like(SysRole::getRoleName, query.getRoleName());
        }
        if (StringUtils.hasText(query.getRoleKey())) {
            wrapper.like(SysRole::getRoleKey, query.getRoleKey());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysRole::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(SysRole::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysRole> selectRolesAll() {
        return this.list(new LambdaQueryWrapper<SysRole>()
            .eq(SysRole::getStatus, "0")
            .orderByAsc(SysRole::getRoleSort));
    }

    @Override
    public SysRole selectRoleById(Long roleId) {
        return this.getById(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertRole(RoleDTO roleDTO) {
        // 检查角色key是否已存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleKey, roleDTO.getRoleKey());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "角色权限字符串已存在");
        }

        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleDTO, role);

        boolean result = this.save(role);

        // 保存菜单关联
        if (result && roleDTO.getMenuIds() != null && roleDTO.getMenuIds().length > 0) {
            insertRoleMenu(role.getRoleId(), roleDTO.getMenuIds());
        }

        // 保存部门关联
        if (result && roleDTO.getDeptIds() != null && roleDTO.getDeptIds().length > 0) {
            insertRoleDept(role.getRoleId(), roleDTO.getDeptIds());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(RoleDTO roleDTO) {
        SysRole existingRole = this.getById(roleDTO.getRoleId());
        if (existingRole == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 检查角色key是否被其他角色使用
        if (!existingRole.getRoleKey().equals(roleDTO.getRoleKey())) {
            LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysRole::getRoleKey, roleDTO.getRoleKey());
            wrapper.ne(SysRole::getRoleId, roleDTO.getRoleId());
            if (this.count(wrapper) > 0) {
                throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "角色权限字符串已存在");
            }
        }

        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleDTO, role);

        boolean result = this.updateById(role);

        // 删除旧的菜单关联
        if (result) {
            deleteRoleMenu(roleDTO.getRoleId());
            // 保存新的菜单关联
            if (roleDTO.getMenuIds() != null && roleDTO.getMenuIds().length > 0) {
                insertRoleMenu(roleDTO.getRoleId(), roleDTO.getMenuIds());
            }
        }

        // 删除旧的部门关联
        if (result) {
            deleteRoleDept(roleDTO.getRoleId());
            // 保存新的部门关联
            if (roleDTO.getDeptIds() != null && roleDTO.getDeptIds().length > 0) {
                insertRoleDept(roleDTO.getRoleId(), roleDTO.getDeptIds());
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long roleId) {
        // 检查是否有关联用户
        // 这里需要查询sys_user_role表，暂时跳过
        return this.removeById(roleId);
    }

    @Override
    public List<TreeSelectDTO> buildRoleTree(List<SysRole> roles) {
        List<TreeSelectDTO> treeList = new ArrayList<>();
        for (SysRole role : roles) {
            TreeSelectDTO dto = new TreeSelectDTO();
            dto.setId(role.getRoleId());
            dto.setLabel(role.getRoleName());
            treeList.add(dto);
        }
        return treeList;
    }

    private void insertRoleMenu(Long roleId, Long[] menuIds) {
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            list.add(roleMenu);
        }
        // 批量插入
        if (!list.isEmpty()) {
            // 这里需要使用批量插入，暂时跳过
        }
    }

    private void deleteRoleMenu(Long roleId) {
        // 删除角色菜单关联
        // 这里需要实现删除逻辑
    }

    private void insertRoleDept(Long roleId, Long[] deptIds) {
        List<SysRoleDept> list = new ArrayList<>();
        for (Long deptId : deptIds) {
            SysRoleDept roleDept = new SysRoleDept();
            roleDept.setRoleId(roleId);
            roleDept.setDeptId(deptId);
            list.add(roleDept);
        }
        // 批量插入
        if (!list.isEmpty()) {
            // 这里需要使用批量插入
        }
    }

    private void deleteRoleDept(Long roleId) {
        // 删除角色部门关联
        // 这里需要实现删除逻辑
    }
}
