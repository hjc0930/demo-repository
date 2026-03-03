package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件上传表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_upload")
public class SysUpload extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "upload_id", type = IdType.INPUT)
    private String uploadId;

    private String tenantId;
    private Integer folderId;
    private Integer size;
    private String fileName;
    private String newFileName;
    private String url;
    private String ext;
    private String mimeType;
    private String storageType;
    private String fileMd5;
    private String thumbnail;
    private String parentFileId;
    private Integer version;
    private Boolean isLatest;
    private Integer downloadCount;
    private String status;
    private String delFlag;
}
