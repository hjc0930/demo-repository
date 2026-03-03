package com.soybean.admin.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色DTO
 */
@Data
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private String dataScope;
    private Boolean menuCheckStrictly;
    private Boolean deptCheckStrictly;
    private String status;
    private String remark;
    private Long[] menuIds;
    private Long[] deptIds;
    private List<Long> userIds;
}
