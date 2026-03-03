package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostDTO extends QueryDTO {

    /** 岗位ID */
    private Long postId;

    /** 部门ID */
    private Long deptId;

    /** 岗位编码 */
    private String postCode;

    /** 岗位类别 */
    private String postCategory;

    /** 岗位名称 */
    private String postName;

    /** 显示顺序 */
    private Integer postSort;

    /** 状态 */
    private String status;
}
