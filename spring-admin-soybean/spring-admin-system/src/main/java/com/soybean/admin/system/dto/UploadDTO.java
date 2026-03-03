package com.soybean.admin.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件上传DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UploadDTO extends QueryDTO {

    /** 文件ID */
    private String uploadId;

    /** 文件夹ID */
    private Integer folderId;

    /** 文件大小 */
    private Integer size;

    /** 文件名 */
    private String fileName;

    /** 新文件名 */
    private String newFileName;

    /** 文件URL */
    private String url;

    /** 文件扩展名 */
    private String ext;

    /** MIME类型 */
    private String mimeType;

    /** 存储类型 */
    private String storageType;

    /** 文件MD5 */
    private String fileMd5;

    /** 缩略图URL */
    private String thumbnail;

    /** 状态 */
    private String status;
}
