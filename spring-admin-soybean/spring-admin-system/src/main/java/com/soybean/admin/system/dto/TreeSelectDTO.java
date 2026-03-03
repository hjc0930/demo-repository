package com.soybean.admin.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 树选择节点DTO
 */
@Data
public class TreeSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    private List<TreeSelectDTO> children;
}
