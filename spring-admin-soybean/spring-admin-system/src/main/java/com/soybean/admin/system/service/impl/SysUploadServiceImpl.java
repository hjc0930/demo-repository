package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysUpload;
import com.soybean.admin.data.mapper.SysUploadMapper;
import com.soybean.admin.system.dto.UploadDTO;
import com.soybean.admin.system.service.SysUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 文件上传服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUploadServiceImpl extends ServiceImpl<SysUploadMapper, SysUpload> implements SysUploadService {

    private final SysUploadMapper uploadMapper;

    @Override
    public IPage<SysUpload> selectUploadPage(Page<SysUpload> page, UploadDTO query) {
        LambdaQueryWrapper<SysUpload> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getFileName())) {
            wrapper.like(SysUpload::getFileName, query.getFileName());
        }
        if (query.getFolderId() != null) {
            wrapper.eq(SysUpload::getFolderId, query.getFolderId());
        }
        if (StringUtils.hasText(query.getExt())) {
            wrapper.eq(SysUpload::getExt, query.getExt());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysUpload::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.like(SysUpload::getFileName, query.getKeyword());
        }

        // 查询未删除的数据
        wrapper.eq(SysUpload::getDelFlag, "0");
        wrapper.orderByDesc(SysUpload::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysUpload> selectUploadList(UploadDTO query) {
        LambdaQueryWrapper<SysUpload> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getFileName())) {
            wrapper.like(SysUpload::getFileName, query.getFileName());
        }
        if (query.getFolderId() != null) {
            wrapper.eq(SysUpload::getFolderId, query.getFolderId());
        }
        if (StringUtils.hasText(query.getExt())) {
            wrapper.eq(SysUpload::getExt, query.getExt());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysUpload::getStatus, query.getStatus());
        }

        wrapper.eq(SysUpload::getDelFlag, "0");
        wrapper.orderByDesc(SysUpload::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysUpload selectUploadById(String uploadId) {
        return this.getById(uploadId);
    }

    @Override
    public List<SysUpload> selectUploadsByFolderId(Integer folderId) {
        LambdaQueryWrapper<SysUpload> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUpload::getFolderId, folderId);
        wrapper.eq(SysUpload::getDelFlag, "0");
        wrapper.orderByDesc(SysUpload::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysUpload selectUploadByMd5(String fileMd5) {
        LambdaQueryWrapper<SysUpload> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUpload::getFileMd5, fileMd5);
        wrapper.eq(SysUpload::getDelFlag, "0");
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUpload(UploadDTO uploadDTO) {
        SysUpload upload = new SysUpload();
        BeanUtils.copyProperties(uploadDTO, upload);
        upload.setDelFlag("0");

        return this.save(upload);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUpload(UploadDTO uploadDTO) {
        SysUpload upload = new SysUpload();
        BeanUtils.copyProperties(uploadDTO, upload);

        return this.updateById(upload);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUpload(String uploadId) {
        // 逻辑删除
        SysUpload upload = new SysUpload();
        upload.setUploadId(uploadId);
        upload.setDelFlag("2");

        return this.updateById(upload);
    }
}
