package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysSmsTemplate;
import com.soybean.admin.data.mapper.SysSmsTemplateMapper;
import com.soybean.admin.system.dto.SmsTemplateDTO;
import com.soybean.admin.system.service.SysSmsTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 短信模板服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysSmsTemplateServiceImpl extends ServiceImpl<SysSmsTemplateMapper, SysSmsTemplate> implements SysSmsTemplateService {

    private final SysSmsTemplateMapper smsTemplateMapper;

    @Override
    public IPage<SysSmsTemplate> selectSmsTemplatePage(Page<SysSmsTemplate> page, SmsTemplateDTO query) {
        LambdaQueryWrapper<SysSmsTemplate> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysSmsTemplate::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.like(SysSmsTemplate::getCode, query.getCode());
        }
        if (query.getType() != null) {
            wrapper.eq(SysSmsTemplate::getType, query.getType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysSmsTemplate::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(SysSmsTemplate::getName, query.getKeyword())
                    .or().like(SysSmsTemplate::getCode, query.getKeyword()));
        }

        wrapper.eq(SysSmsTemplate::getDelFlag, "0");
        wrapper.orderByDesc(SysSmsTemplate::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysSmsTemplate> selectSmsTemplateList(SmsTemplateDTO query) {
        LambdaQueryWrapper<SysSmsTemplate> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysSmsTemplate::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.like(SysSmsTemplate::getCode, query.getCode());
        }
        if (query.getType() != null) {
            wrapper.eq(SysSmsTemplate::getType, query.getType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysSmsTemplate::getStatus, query.getStatus());
        }

        wrapper.eq(SysSmsTemplate::getDelFlag, "0");
        wrapper.orderByDesc(SysSmsTemplate::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysSmsTemplate selectSmsTemplateById(Long id) {
        return this.getById(id);
    }

    @Override
    public SysSmsTemplate selectSmsTemplateByCode(String code) {
        LambdaQueryWrapper<SysSmsTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysSmsTemplate::getCode, code);
        wrapper.eq(SysSmsTemplate::getDelFlag, "0");
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSmsTemplate(SmsTemplateDTO smsTemplateDTO) {
        // 检查模板编码是否已存在
        SysSmsTemplate existing = selectSmsTemplateByCode(smsTemplateDTO.getCode());
        if (existing != null) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "模板编码已存在");
        }

        SysSmsTemplate template = new SysSmsTemplate();
        BeanUtils.copyProperties(smsTemplateDTO, template);
        template.setDelFlag("0");

        return this.save(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSmsTemplate(SmsTemplateDTO smsTemplateDTO) {
        SysSmsTemplate existing = this.getById(smsTemplateDTO.getId());
        if (existing == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 检查模板编码是否被其他模板使用
        if (!existing.getCode().equals(smsTemplateDTO.getCode())) {
            SysSmsTemplate duplicate = selectSmsTemplateByCode(smsTemplateDTO.getCode());
            if (duplicate != null && !duplicate.getId().equals(smsTemplateDTO.getId())) {
                throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "模板编码已存在");
            }
        }

        SysSmsTemplate template = new SysSmsTemplate();
        BeanUtils.copyProperties(smsTemplateDTO, template);

        return this.updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSmsTemplate(Long id) {
        // 逻辑删除
        SysSmsTemplate template = new SysSmsTemplate();
        template.setId(id);
        template.setDelFlag("2");

        return this.updateById(template);
    }
}
