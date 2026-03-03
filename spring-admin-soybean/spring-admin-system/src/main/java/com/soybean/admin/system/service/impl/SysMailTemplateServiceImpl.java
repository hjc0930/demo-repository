package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysMailTemplate;
import com.soybean.admin.data.mapper.SysMailTemplateMapper;
import com.soybean.admin.system.dto.MailTemplateDTO;
import com.soybean.admin.system.service.SysMailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 邮件模板服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMailTemplateServiceImpl extends ServiceImpl<SysMailTemplateMapper, SysMailTemplate> implements SysMailTemplateService {

    private final SysMailTemplateMapper mailTemplateMapper;

    @Override
    public IPage<SysMailTemplate> selectMailTemplatePage(Page<SysMailTemplate> page, MailTemplateDTO query) {
        LambdaQueryWrapper<SysMailTemplate> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysMailTemplate::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.like(SysMailTemplate::getCode, query.getCode());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysMailTemplate::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(SysMailTemplate::getName, query.getKeyword())
                    .or().like(SysMailTemplate::getCode, query.getKeyword()));
        }

        wrapper.eq(SysMailTemplate::getDelFlag, "0");
        wrapper.orderByDesc(SysMailTemplate::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysMailTemplate> selectMailTemplateList(MailTemplateDTO query) {
        LambdaQueryWrapper<SysMailTemplate> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SysMailTemplate::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.like(SysMailTemplate::getCode, query.getCode());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysMailTemplate::getStatus, query.getStatus());
        }

        wrapper.eq(SysMailTemplate::getDelFlag, "0");
        wrapper.orderByDesc(SysMailTemplate::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysMailTemplate selectMailTemplateById(Long id) {
        return this.getById(id);
    }

    @Override
    public SysMailTemplate selectMailTemplateByCode(String code) {
        LambdaQueryWrapper<SysMailTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMailTemplate::getCode, code);
        wrapper.eq(SysMailTemplate::getDelFlag, "0");
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertMailTemplate(MailTemplateDTO mailTemplateDTO) {
        // 检查模板编码是否已存在
        SysMailTemplate existing = selectMailTemplateByCode(mailTemplateDTO.getCode());
        if (existing != null) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "模板编码已存在");
        }

        SysMailTemplate template = new SysMailTemplate();
        BeanUtils.copyProperties(mailTemplateDTO, template);
        template.setDelFlag("0");

        return this.save(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMailTemplate(MailTemplateDTO mailTemplateDTO) {
        SysMailTemplate existing = this.getById(mailTemplateDTO.getId());
        if (existing == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 检查模板编码是否被其他模板使用
        if (!existing.getCode().equals(mailTemplateDTO.getCode())) {
            SysMailTemplate duplicate = selectMailTemplateByCode(mailTemplateDTO.getCode());
            if (duplicate != null && !duplicate.getId().equals(mailTemplateDTO.getId())) {
                throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "模板编码已存在");
            }
        }

        SysMailTemplate template = new SysMailTemplate();
        BeanUtils.copyProperties(mailTemplateDTO, template);

        return this.updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMailTemplate(Long id) {
        // 逻辑删除
        SysMailTemplate template = new SysMailTemplate();
        template.setId(id);
        template.setDelFlag("2");

        return this.updateById(template);
    }
}
