package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysFileFolder;
import com.soybean.admin.system.dto.FileFolderDTO;

import java.util.List;

/**
 * 文件夹服务接口
 */
public interface SysFileFolderService extends IService<SysFileFolder> {

    /**
     * 分页查询文件夹
     */
    IPage<SysFileFolder> selectFileFolderPage(Page<SysFileFolder> page, FileFolderDTO query);

    /**
     * 查询文件夹列表
     */
    List<SysFileFolder> selectFileFolderList(FileFolderDTO query);

    /**
     * 查询文件夹树
     */
    List<SysFileFolder> selectFileFolderTree(FileFolderDTO query);

    /**
     * 根据ID查询文件夹
     */
    SysFileFolder selectFileFolderById(Long folderId);

    /**
     * 根据父ID查询子文件夹
     */
    List<SysFileFolder> selectFileFoldersByParentId(Integer parentId);

    /**
     * 新增文件夹
     */
    boolean insertFileFolder(FileFolderDTO fileFolderDTO);

    /**
     * 修改文件夹
     */
    boolean updateFileFolder(FileFolderDTO fileFolderDTO);

    /**
     * 删除文件夹
     */
    boolean deleteFileFolder(Long folderId);

    /**
     * 构建文件夹树
     */
    List<SysFileFolder> buildFileFolderTree(List<SysFileFolder> folders);
}
