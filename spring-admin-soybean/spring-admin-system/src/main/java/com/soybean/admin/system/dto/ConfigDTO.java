package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统配置DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigDTO extends QueryDTO {

    /** 配置ID */
    private Long configId;

    /** 配置名称 */
    private String configName;

    /** 配置键名 */
    private String configKey;

    /** 配置键值 */
    private String configValue;

    /** 系统内置 */
    private String configType;

    /** 状态 */
    private String status;
}
