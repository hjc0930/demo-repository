package com.soybean.admin.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 路由VO
 */
@Data
public class RouterVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 路由名字 */
    private String name;

    /** 路由地址 */
    private String path;

    /** 重定向地址 */
    private String redirect;

    /** 组件地址 */
    private String component;

    /** 路由参数 */
    private String query;

    /** 是否为外链 */
    private Boolean isFrame;

    /** 是否缓存 */
    private Boolean isCache;

    /** 菜单类型 */
    private String menuType;

    /** 菜单显示状态 */
    private String visible;

    /** 菜单状态 */
    private String status;

    /** 权限标识 */
    private String perms;

    /** 菜单图标 */
    private String icon;

    /** 子菜单 */
    private List<RouterVO> children;
}
