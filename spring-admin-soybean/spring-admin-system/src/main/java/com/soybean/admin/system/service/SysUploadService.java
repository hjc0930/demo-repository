package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysUpload;
import com.soybean.admin.system.dto.UploadDTO;

import java.util.List;

/**
 * 文件上传服务接口
 */
public interface SysUploadService extends IService<SysUpload> {

    /**
     * 分页查询文件
     */
    IPage<SysUpload> selectUploadPage(Page<SysUpload> page, UploadDTO query);

    /**
     * 查询文件列表
     */
    List<SysUpload> selectUploadList(UploadDTO query);

    /**
     * 根据ID查询文件
     */
    SysUpload selectUploadById(String uploadId);

    /**
     * 根据文件夹ID查询文件
     */
    List<SysUpload> selectUploadsByFolderId(Integer folderId);

    /**
     * 根据MD5查询文件
     */
    SysUpload selectUploadByMd5(String fileMd5);

    /**
     * 新增文件记录
     */
    boolean insertUpload(UploadDTO uploadDTO);

    /**
     * 修改文件记录
     */
    boolean updateUpload(UploadDTO uploadDTO);

    /**
     * 删除文件记录
     */
    boolean deleteUpload(String uploadId);
}
