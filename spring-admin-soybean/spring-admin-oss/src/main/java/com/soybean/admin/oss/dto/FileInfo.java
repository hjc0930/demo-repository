package com.soybean.admin.oss.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息DTO
 */
@Data
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型（MIME类型）
     */
    private String fileType;

    /**
     * 存储配置ID
     */
    private Long configId;

    /**
     * 存储提供商
     */
    private String provider;

    /**
     * 存储路径
     */
    private String filePath;

    /**
     * 访问URL
     */
    private String url;

    /**
     * 文件MD5
     */
    private String fileMd5;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;
}
