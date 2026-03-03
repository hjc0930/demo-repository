package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysMenu;
import com.soybean.admin.system.dto.MenuDTO;
import com.soybean.admin.system.dto.RouterVO;
import com.soybean.admin.system.dto.TreeSelectDTO;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 查询所有菜单
     */
    List<SysMenu> selectMenusAll();

    /**
     * 根据用户ID查询菜单列表
     */
    List<SysMenu> selectMenusByUserId(Long userId);

    /**
     * 根据角色ID查询菜单列表
     */
    List<SysMenu> selectMenusByRoleId(Long roleId);

    /**
     * 查询菜单树
     */
    List<SysMenu> selectMenuTree(SysMenu menu);

    /**
     * 根据菜单ID查询菜单
     */
    SysMenu selectMenuById(Long menuId);

    /**
     * 新增菜单
     */
    boolean insertMenu(MenuDTO menuDTO);

    /**
     * 修改菜单
     */
    boolean updateMenu(MenuDTO menuDTO);

    /**
     * 删除菜单
     */
    boolean deleteMenu(Long menuId);

    /**
     * 构建路由列表
     */
    List<RouterVO> buildRouters(Long userId);

    /**
     * 构建菜单树选择
     */
    List<TreeSelectDTO> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * 检查是否存在子菜单
     */
    boolean hasChildByMenuId(Long menuId);

    /**
     * 根据角色ID查询菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(Long roleId);
}
