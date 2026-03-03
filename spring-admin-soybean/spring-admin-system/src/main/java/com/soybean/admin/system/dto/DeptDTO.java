package com.soybean.admin.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 部门DTO
 */
@Data
public class DeptDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long deptId;
    private Long parentId;
    private String ancestors;
    private String deptName;
    private Integer orderNum;
    private String leader;
    private String phone;
    private String email;
    private String status;
    private String remark;
    private List<Long> userIds;
}
