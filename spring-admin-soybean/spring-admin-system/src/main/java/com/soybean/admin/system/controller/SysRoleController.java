package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysRole;
import com.soybean.admin.system.dto.RoleDTO;
import com.soybean.admin.system.dto.TreeSelectDTO;
import com.soybean.admin.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/page")
    @RequirePermission("system:role:list")
    public Result<IPage<SysRole>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            RoleDTO query) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        IPage<SysRole> result = roleService.selectRolePage(page, query);
        return Result.page(result.getRecords(), result.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/all")
    @RequirePermission("system:role:list")
    public Result<List<SysRole>> all() {
        List<SysRole> roles = roleService.selectRolesAll();
        return Result.ok(roles);
    }

    /**
     * 根据角色ID查询角色
     */
    @GetMapping("/{roleId}")
    @RequirePermission("system:role:query")
    public Result<SysRole> getRole(@PathVariable Long roleId) {
        SysRole role = roleService.selectRoleById(roleId);
        return Result.ok(role);
    }

    /**
     * 新增角色
     */
    @PostMapping
    @RequirePermission("system:role:add")
    public Result<Void> add(@RequestBody RoleDTO roleDTO) {
        roleService.insertRole(roleDTO);
        return Result.ok();
    }

    /**
     * 修改角色
     */
    @PutMapping
    @RequirePermission("system:role:edit")
    public Result<Void> edit(@RequestBody RoleDTO roleDTO) {
        roleService.updateRole(roleDTO);
        return Result.ok();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleIds}")
    @RequirePermission("system:role:remove")
    public Result<Void> remove(@PathVariable Long[] roleIds) {
        for (Long roleId : roleIds) {
            roleService.deleteRole(roleId);
        }
        return Result.ok();
    }

    /**
     * 构建角色树
     */
    @GetMapping("/tree")
    @RequirePermission("system:role:list")
    public Result<List<TreeSelectDTO>> tree() {
        List<SysRole> roles = roleService.selectRolesAll();
        List<TreeSelectDTO> tree = roleService.buildRoleTree(roles);
        return Result.ok(tree);
    }
}
