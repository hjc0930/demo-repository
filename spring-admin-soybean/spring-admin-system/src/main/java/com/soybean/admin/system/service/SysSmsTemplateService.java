package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysSmsTemplate;
import com.soybean.admin.system.dto.SmsTemplateDTO;

import java.util.List;

/**
 * 短信模板服务接口
 */
public interface SysSmsTemplateService extends IService<SysSmsTemplate> {

    /**
     * 分页查询短信模板
     */
    IPage<SysSmsTemplate> selectSmsTemplatePage(Page<SysSmsTemplate> page, SmsTemplateDTO query);

    /**
     * 查询短信模板列表
     */
    List<SysSmsTemplate> selectSmsTemplateList(SmsTemplateDTO query);

    /**
     * 根据ID查询短信模板
     */
    SysSmsTemplate selectSmsTemplateById(Long id);

    /**
     * 根据编码查询短信模板
     */
    SysSmsTemplate selectSmsTemplateByCode(String code);

    /**
     * 新增短信模板
     */
    boolean insertSmsTemplate(SmsTemplateDTO smsTemplateDTO);

    /**
     * 修改短信模板
     */
    boolean updateSmsTemplate(SmsTemplateDTO smsTemplateDTO);

    /**
     * 删除短信模板
     */
    boolean deleteSmsTemplate(Long id);
}
