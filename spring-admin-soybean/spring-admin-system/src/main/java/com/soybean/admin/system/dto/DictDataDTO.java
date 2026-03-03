package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictDataDTO extends QueryDTO {

    /** 字典编码 */
    private Long dictCode;

    /** 字典排序 */
    private Integer dictSort;

    /** 字典标签 */
    private String dictLabel;

    /** 字典键值 */
    private String dictValue;

    /** 字典类型 */
    private String dictType;

    /** CSS类名 */
    private String cssClass;

    /** 列表样式 */
    private String listClass;

    /** 是否默认 */
    private String isDefault;

    /** 状态 */
    private String status;
}
