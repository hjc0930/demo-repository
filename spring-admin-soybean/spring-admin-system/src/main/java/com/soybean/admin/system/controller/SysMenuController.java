package com.soybean.admin.system.controller;

import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysMenu;
import com.soybean.admin.system.dto.MenuDTO;
import com.soybean.admin.system.dto.RouterVO;
import com.soybean.admin.system.dto.TreeSelectDTO;
import com.soybean.admin.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/api/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    /**
     * 查询所有菜单
     */
    @GetMapping("/list")
    @RequirePermission("system:menu:list")
    public Result<List<SysMenu>> list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenusAll();
        return Result.ok(menus);
    }

    /**
     * 根据用户ID查询菜单树
     */
    @GetMapping("/user/{userId}")
    @RequirePermission("system:menu:query")
    public Result<List<SysMenu>> userMenu(@PathVariable Long userId) {
        List<SysMenu> menus = menuService.selectMenusByUserId(userId);
        return Result.ok(menus);
    }

    /**
     * 查询菜单树
     */
    @GetMapping("/tree")
    @RequirePermission("system:menu:list")
    public Result<List<SysMenu>> tree(SysMenu menu) {
        List<SysMenu> tree = menuService.selectMenuTree(menu);
        return Result.ok(tree);
    }

    /**
     * 根据角色ID查询菜单列表
     */
    @GetMapping("/role/{roleId}")
    @RequirePermission("system:menu:query")
    public Result<List<Long>> roleMenu(@PathVariable Long roleId) {
        List<Long> menuIds = menuService.selectMenuIdsByRoleId(roleId);
        return Result.ok(menuIds);
    }

    /**
     * 根据菜单ID查询菜单
     */
    @GetMapping("/{menuId}")
    @RequirePermission("system:menu:query")
    public Result<SysMenu> getMenu(@PathVariable Long menuId) {
        SysMenu menu = menuService.selectMenuById(menuId);
        return Result.ok(menu);
    }

    /**
     * 新增菜单
     */
    @PostMapping
    @RequirePermission("system:menu:add")
    public Result<Void> add(@RequestBody MenuDTO menuDTO) {
        menuService.insertMenu(menuDTO);
        return Result.ok();
    }

    /**
     * 修改菜单
     */
    @PutMapping
    @RequirePermission("system:menu:edit")
    public Result<Void> edit(@RequestBody MenuDTO menuDTO) {
        menuService.updateMenu(menuDTO);
        return Result.ok();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    @RequirePermission("system:menu:remove")
    public Result<Void> remove(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return Result.ok();
    }

    /**
     * 构建路由列表
     */
    @GetMapping("/routers")
    @RequirePermission("system:menu:query")
    public Result<List<RouterVO>> routers() {
        // TODO: 从当前登录用户获取userId
        Long userId = 1L;
        List<RouterVO> routers = menuService.buildRouters(userId);
        return Result.ok(routers);
    }

    /**
     * 构建菜单树选择
     */
    @GetMapping("/tree-select")
    @RequirePermission("system:menu:query")
    public Result<List<TreeSelectDTO>> treeSelect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuTree(menu);
        List<TreeSelectDTO> tree = menuService.buildMenuTreeSelect(menus);
        return Result.ok(tree);
    }
}
