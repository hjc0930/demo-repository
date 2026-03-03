package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件夹DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileFolderDTO extends QueryDTO {

    /** 文件夹ID */
    private Long folderId;

    /** 父文件夹ID */
    private Integer parentId;

    /** 文件夹名称 */
    private String folderName;

    /** 文件夹路径 */
    private String folderPath;

    /** 显示顺序 */
    private Integer orderNum;

    /** 状态 */
    private String status;
}
