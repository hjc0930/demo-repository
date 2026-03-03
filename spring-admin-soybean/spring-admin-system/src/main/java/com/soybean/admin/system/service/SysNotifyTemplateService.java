package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysNotifyTemplate;
import com.soybean.admin.system.dto.NotifyTemplateDTO;

import java.util.List;

/**
 * 站内信模板服务接口
 */
public interface SysNotifyTemplateService extends IService<SysNotifyTemplate> {

    /**
     * 分页查询站内信模板
     */
    IPage<SysNotifyTemplate> selectNotifyTemplatePage(Page<SysNotifyTemplate> page, NotifyTemplateDTO query);

    /**
     * 查询站内信模板列表
     */
    List<SysNotifyTemplate> selectNotifyTemplateList(NotifyTemplateDTO query);

    /**
     * 根据ID查询站内信模板
     */
    SysNotifyTemplate selectNotifyTemplateById(Long id);

    /**
     * 根据编码查询站内信模板
     */
    SysNotifyTemplate selectNotifyTemplateByCode(String code);

    /**
     * 新增站内信模板
     */
    boolean insertNotifyTemplate(NotifyTemplateDTO notifyTemplateDTO);

    /**
     * 修改站内信模板
     */
    boolean updateNotifyTemplate(NotifyTemplateDTO notifyTemplateDTO);

    /**
     * 删除站内信模板
     */
    boolean deleteNotifyTemplate(Long id);
}
