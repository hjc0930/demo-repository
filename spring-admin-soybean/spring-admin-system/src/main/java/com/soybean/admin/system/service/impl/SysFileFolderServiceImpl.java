package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.soybean.admin.data.entity.SysFileFolder;
import com.soybean.admin.data.mapper.SysFileFolderMapper;
import com.soybean.admin.system.dto.FileFolderDTO;
import com.soybean.admin.system.service.SysFileFolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件夹服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysFileFolderServiceImpl extends ServiceImpl<SysFileFolderMapper, SysFileFolder> implements SysFileFolderService {

    private final SysFileFolderMapper fileFolderMapper;

    @Override
    public IPage<SysFileFolder> selectFileFolderPage(Page<SysFileFolder> page, FileFolderDTO query) {
        LambdaQueryWrapper<SysFileFolder> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getFolderName())) {
            wrapper.like(SysFileFolder::getFolderName, query.getFolderName());
        }
        if (query.getParentId() != null) {
            wrapper.eq(SysFileFolder::getParentId, query.getParentId());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysFileFolder::getStatus, query.getStatus());
        }

        // 查询未删除的数据
        wrapper.eq(SysFileFolder::getDelFlag, "0");
        wrapper.orderByAsc(SysFileFolder::getOrderNum);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysFileFolder> selectFileFolderList(FileFolderDTO query) {
        LambdaQueryWrapper<SysFileFolder> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getFolderName())) {
            wrapper.like(SysFileFolder::getFolderName, query.getFolderName());
        }
        if (query.getParentId() != null) {
            wrapper.eq(SysFileFolder::getParentId, query.getParentId());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysFileFolder::getStatus, query.getStatus());
        }

        wrapper.eq(SysFileFolder::getDelFlag, "0");
        wrapper.orderByAsc(SysFileFolder::getOrderNum);
        return this.list(wrapper);
    }

    @Override
    public List<SysFileFolder> selectFileFolderTree(FileFolderDTO query) {
        List<SysFileFolder> folders = selectFileFolderList(query);
        return buildFileFolderTree(folders);
    }

    @Override
    public SysFileFolder selectFileFolderById(Long folderId) {
        return this.getById(folderId);
    }

    @Override
    public List<SysFileFolder> selectFileFoldersByParentId(Integer parentId) {
        LambdaQueryWrapper<SysFileFolder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysFileFolder::getParentId, parentId);
        wrapper.eq(SysFileFolder::getDelFlag, "0");
        wrapper.orderByAsc(SysFileFolder::getOrderNum);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertFileFolder(FileFolderDTO fileFolderDTO) {
        SysFileFolder fileFolder = new SysFileFolder();
        BeanUtils.copyProperties(fileFolderDTO, fileFolder);
        fileFolder.setDelFlag("0");

        return this.save(fileFolder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFileFolder(FileFolderDTO fileFolderDTO) {
        SysFileFolder fileFolder = new SysFileFolder();
        BeanUtils.copyProperties(fileFolderDTO, fileFolder);

        return this.updateById(fileFolder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFileFolder(Long folderId) {
        // 检查是否有子文件夹
        LambdaQueryWrapper<SysFileFolder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysFileFolder::getParentId, folderId);
        wrapper.eq(SysFileFolder::getDelFlag, "0");
        long count = this.count(wrapper);
        if (count > 0) {
            // TODO: 抛出异常，不能删除有子文件夹的文件夹
        }

        // 逻辑删除
        SysFileFolder fileFolder = new SysFileFolder();
        fileFolder.setFolderId(folderId);
        fileFolder.setDelFlag("2");

        return this.updateById(fileFolder);
    }

    @Override
    public List<SysFileFolder> buildFileFolderTree(List<SysFileFolder> folders) {
        List<SysFileFolder> returnList = new ArrayList<>();
        List<Long> tempList = folders.stream().map(SysFileFolder::getFolderId).collect(Collectors.toList());

        for (SysFileFolder folder : folders) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(folder.getParentId())) {
                recursionFn(folders, folder);
                returnList.add(folder);
            }
        }

        if (returnList.isEmpty()) {
            returnList = folders;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysFileFolder> list, SysFileFolder t) {
        // 得到子节点列表
        List<SysFileFolder> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysFileFolder tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysFileFolder> getChildList(List<SysFileFolder> list, SysFileFolder t) {
        List<SysFileFolder> tlist = new ArrayList<>();
        Iterator<SysFileFolder> it = list.iterator();
        while (it.hasNext()) {
            SysFileFolder n = it.next();
            if (n.getParentId() != null && n.getParentId().equals(t.getFolderId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysFileFolder> list, SysFileFolder t) {
        return !getChildList(list, t).isEmpty();
    }
}
