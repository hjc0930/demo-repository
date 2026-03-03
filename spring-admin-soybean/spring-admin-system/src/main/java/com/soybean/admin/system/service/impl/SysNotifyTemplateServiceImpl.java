package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysNotifyTemplate;
import com.soybean.admin.data.mapper.SysNotifyTemplateMapper;
import com.soybean.admin.system.dto.NotifyTemplateDTO;
import com.soybean.admin.system.service.SysNotifyTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 站内信模板服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysNotifyTemplateServiceImpl extends ServiceImpl<SysNotifyTemplateMapper, SysNotifyTemplate> implements SysNotifyTemplateService {

    private final SysNotifyTemplateMapper notifyTemplateMapper;

    @Override
    public IPage<SysNotifyTemplate> selectNotifyTemplatePage(Page<SysNotifyTemplate> page, NotifyTemplateDTO query) {
        LambdaQueryWrapper<SysNotifyTemplate> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysNotifyTemplate::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.like(SysNotifyTemplate::getCode, query.getCode());
        }
        if (query.getType() != null) {
            wrapper.eq(SysNotifyTemplate::getType, query.getType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysNotifyTemplate::getStatus, query.getStatus());
        }

        wrapper.eq(SysNotifyTemplate::getDelFlag, "0");
        wrapper.orderByDesc(SysNotifyTemplate::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysNotifyTemplate> selectNotifyTemplateList(NotifyTemplateDTO query) {
        LambdaQueryWrapper<SysNotifyTemplate> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysNotifyTemplate::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.like(SysNotifyTemplate::getCode, query.getCode());
        }
        if (query.getType() != null) {
            wrapper.eq(SysNotifyTemplate::getType, query.getType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysNotifyTemplate::getStatus, query.getStatus());
        }

        wrapper.eq(SysNotifyTemplate::getDelFlag, "0");
        wrapper.orderByDesc(SysNotifyTemplate::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysNotifyTemplate selectNotifyTemplateById(Long id) {
        return this.getById(id);
    }

    @Override
    public SysNotifyTemplate selectNotifyTemplateByCode(String code) {
        LambdaQueryWrapper<SysNotifyTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysNotifyTemplate::getCode, code);
        wrapper.eq(SysNotifyTemplate::getDelFlag, "0");
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertNotifyTemplate(NotifyTemplateDTO notifyTemplateDTO) {
        SysNotifyTemplate existing = selectNotifyTemplateByCode(notifyTemplateDTO.getCode());
        if (existing != null) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "模板编码已存在");
        }

        SysNotifyTemplate template = new SysNotifyTemplate();
        BeanUtils.copyProperties(notifyTemplateDTO, template);
        template.setDelFlag("0");

        return this.save(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNotifyTemplate(NotifyTemplateDTO notifyTemplateDTO) {
        SysNotifyTemplate existing = this.getById(notifyTemplateDTO.getId());
        if (existing == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        if (!existing.getCode().equals(notifyTemplateDTO.getCode())) {
            SysNotifyTemplate duplicate = selectNotifyTemplateByCode(notifyTemplateDTO.getCode());
            if (duplicate != null && !duplicate.getId().equals(notifyTemplateDTO.getId())) {
                throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "模板编码已存在");
            }
        }

        SysNotifyTemplate template = new SysNotifyTemplate();
        BeanUtils.copyProperties(notifyTemplateDTO, template);

        return this.updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNotifyTemplate(Long id) {
        SysNotifyTemplate template = new SysNotifyTemplate();
        template.setId(id);
        template.setDelFlag("2");

        return this.updateById(template);
    }
}
