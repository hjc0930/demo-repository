package com.soybean.admin.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单DTO
 */
@Data
public class MenuDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long menuId;
    private Long parentId;
    private Integer orderNum;
    private String path;
    private String component;
    private String query;
    private String isFrame;
    private String isCache;
    private String menuType;
    private String visible;
    private String status;
    private String perms;
    private String icon;
    private String remark;
    private Long[] menuIds;
}
