package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysMailTemplate;
import com.soybean.admin.system.dto.MailTemplateDTO;

import java.util.List;

/**
 * 邮件模板服务接口
 */
public interface SysMailTemplateService extends IService<SysMailTemplate> {

    /**
     * 分页查询邮件模板
     */
    IPage<SysMailTemplate> selectMailTemplatePage(Page<SysMailTemplate> page, MailTemplateDTO query);

    /**
     * 查询邮件模板列表
     */
    List<SysMailTemplate> selectMailTemplateList(MailTemplateDTO query);

    /**
     * 根据ID查询邮件模板
     */
    SysMailTemplate selectMailTemplateById(Long id);

    /**
     * 根据编码查询邮件模板
     */
    SysMailTemplate selectMailTemplateByCode(String code);

    /**
     * 新增邮件模板
     */
    boolean insertMailTemplate(MailTemplateDTO mailTemplateDTO);

    /**
     * 修改邮件模板
     */
    boolean updateMailTemplate(MailTemplateDTO mailTemplateDTO);

    /**
     * 删除邮件模板
     */
    boolean deleteMailTemplate(Long id);
}
