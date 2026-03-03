package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictTypeDTO extends QueryDTO {

    /** 字典ID */
    private Long dictId;

    /** 字典名称 */
    private String dictName;

    /** 字典类型 */
    private String dictType;

    /** 状态 */
    private String status;
}
