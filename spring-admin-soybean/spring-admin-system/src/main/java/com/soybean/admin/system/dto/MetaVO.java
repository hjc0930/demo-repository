package com.soybean.admin.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Meta信息VO
 */
@Data
public class MetaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 菜单标题 */
    private String title;

    /** 菜单图标 */
    private String icon;

    /** 是否缓存 */
    private Boolean noCache;

    /** 链接地址 */
    private String link;
}
