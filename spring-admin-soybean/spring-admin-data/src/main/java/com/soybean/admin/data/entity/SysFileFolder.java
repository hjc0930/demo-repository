package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 文件夹表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file_folder")
public class SysFileFolder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "folder_id", type = IdType.AUTO)
    private Long folderId;

    private String tenantId;
    private Integer parentId;
    private String folderName;
    private String folderPath;
    private Integer orderNum;
    private String status;
    private String delFlag;

    /**
     * 子文件夹列表
     */
    @TableField(exist = false)
    private List<SysFileFolder> children;
}
