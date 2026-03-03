package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysMenu;
import com.soybean.admin.data.mapper.SysMenuMapper;
import com.soybean.admin.system.dto.MenuDTO;
import com.soybean.admin.system.dto.RouterVO;
import com.soybean.admin.system.dto.TreeSelectDTO;
import com.soybean.admin.system.service.SysMenuService;
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
 * 菜单服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> selectMenusAll() {
        return this.list(new LambdaQueryWrapper<SysMenu>()
            .eq(SysMenu::getStatus, "0")
            .orderByAsc(SysMenu::getOrderNum));
    }

    @Override
    public List<SysMenu> selectMenusByUserId(Long userId) {
        return menuMapper.selectMenusByUserId(userId);
    }

    @Override
    public List<SysMenu> selectMenusByRoleId(Long roleId) {
        return menuMapper.selectMenusByRoleId(roleId);
    }

    @Override
    public List<SysMenu> selectMenuTree(SysMenu menu) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menu.getMenuName()), SysMenu::getMenuName, menu.getMenuName())
            .eq(StringUtils.hasText(menu.getStatus()), SysMenu::getStatus, menu.getStatus())
            .orderByAsc(SysMenu::getOrderNum);

        List<SysMenu> menus = this.list(wrapper);
        return buildMenuTree(menus, 0L);
    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        return this.getById(menuId);
    }

    @Override
    public boolean insertMenu(MenuDTO menuDTO) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuDTO, menu);

        // 处理父菜单ID
        if (menuDTO.getParentId() == null || menuDTO.getParentId() == 0) {
            menu.setParentId(0L);
        }

        return this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(MenuDTO menuDTO) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuDTO, menu);

        // 检查是否存在子菜单
        if (menu.getParentId().equals(menu.getMenuId())) {
            throw new RuntimeException("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }

        return this.updateById(menu);
    }

    @Override
    public boolean deleteMenu(Long menuId) {
        // 检查是否有子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, menuId);
        long count = this.count(wrapper);
        if (count > 0) {
            throw new RuntimeException("存在子菜单,不允许删除");
        }

        return this.removeById(menuId);
    }

    @Override
    public List<RouterVO> buildRouters(Long userId) {
        List<SysMenu> menus = menuMapper.selectMenusByUserId(userId);
        List<SysMenu> menuList = menus.stream()
            .filter(m -> "C".equals(m.getMenuType()))
            .collect(Collectors.toList());

        return buildRouters(menuList, 0L);
    }

    @Override
    public List<TreeSelectDTO> buildMenuTreeSelect(List<SysMenu> menus) {
        List<TreeSelectDTO> trees = new ArrayList<>();
        List<SysMenu> rootMenus = menus.stream()
            .filter(m -> m.getParentId() == 0)
            .collect(Collectors.toList());

        for (SysMenu menu : rootMenus) {
            TreeSelectDTO dto = new TreeSelectDTO();
            dto.setId(menu.getMenuId());
            dto.setLabel(menu.getMenuName());
            dto.setChildren(buildMenuTreeSelectChildren(menus, menu.getMenuId()));
            trees.add(dto);
        }

        return trees;
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, menuId);
        return this.count(wrapper) > 0;
    }

    @Override
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        return menuMapper.selectMenuIdsByRoleId(roleId);
    }

    /**
     * 构建菜单树
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menus, Long parentId) {
        List<SysMenu> treeList = new ArrayList<>();

        for (SysMenu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                treeList.add(menu);
                // 递归查找子菜单
                menu.setChildren(buildMenuTree(menus, menu.getMenuId()));
            }
        }

        return treeList;
    }

    /**
     * 构建路由
     */
    private List<RouterVO> buildRouters(List<SysMenu> menus, Long parentId) {
        List<RouterVO> routers = new ArrayList<>();

        for (SysMenu menu : menus) {
            RouterVO router = new RouterVO();
            router.setName(menu.getMenuName());
            router.setPath(menu.getPath());
            router.setComponent(menu.getComponent());
            router.setRedirect("");
            router.setQuery(menu.getQuery());
            router.setIsFrame("1".equals(menu.getIsFrame()));
            router.setIsCache("0".equals(menu.getIsCache()));
            router.setMenuType(menu.getMenuType());
            router.setVisible(menu.getVisible());
            router.setStatus(menu.getStatus());
            router.setPerms(menu.getPerms());
            router.setIcon(menu.getIcon());

            // 递归查找子路由
            router.setChildren(buildRouters(menus, menu.getMenuId()));
            routers.add(router);
        }

        return routers;
    }

    /**
     * 构建菜单树选择
     */
    private List<TreeSelectDTO> buildMenuTreeSelectChildren(List<SysMenu> menus, Long parentId) {
        List<TreeSelectDTO> children = new ArrayList<>();

        for (SysMenu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                TreeSelectDTO dto = new TreeSelectDTO();
                dto.setId(menu.getMenuId());
                dto.setLabel(menu.getMenuName());
                dto.setChildren(buildMenuTreeSelectChildren(menus, menu.getMenuId()));
                children.add(dto);
            }
        }

        return children;
    }
}
