package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysNotifyTemplate;
import com.soybean.admin.system.dto.NotifyTemplateDTO;
import com.soybean.admin.system.service.SysNotifyTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 站内信模板控制器
 */
@RestController
@RequestMapping("/api/system/notify/template")
@RequiredArgsConstructor
public class SysNotifyTemplateController {

    private final SysNotifyTemplateService notifyTemplateService;

    /**
     * 分页查询站内信模板
     */
    @GetMapping("/page")
    @RequirePermission("system:notify:template:list")
    public Result<IPage<SysNotifyTemplate>> page(Page<SysNotifyTemplate> page, NotifyTemplateDTO query) {
        IPage<SysNotifyTemplate> result = notifyTemplateService.selectNotifyTemplatePage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询站内信模板列表
     */
    @GetMapping("/list")
    @RequirePermission("system:notify:template:list")
    public Result<List<SysNotifyTemplate>> list(NotifyTemplateDTO query) {
        List<SysNotifyTemplate> list = notifyTemplateService.selectNotifyTemplateList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询站内信模板
     */
    @GetMapping("/{id}")
    @RequirePermission("system:notify:template:query")
    public Result<SysNotifyTemplate> getNotifyTemplate(@PathVariable Long id) {
        SysNotifyTemplate template = notifyTemplateService.selectNotifyTemplateById(id);
        return Result.ok(template);
    }

    /**
     * 根据编码查询站内信模板
     */
    @GetMapping("/code/{code}")
    @RequirePermission("system:notify:template:query")
    public Result<SysNotifyTemplate> getNotifyTemplateByCode(@PathVariable String code) {
        SysNotifyTemplate template = notifyTemplateService.selectNotifyTemplateByCode(code);
        return Result.ok(template);
    }

    /**
     * 新增站内信模板
     */
    @PostMapping
    @RequirePermission("system:notify:template:add")
    @Log(title = "站内信模板", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody NotifyTemplateDTO notifyTemplateDTO) {
        notifyTemplateService.insertNotifyTemplate(notifyTemplateDTO);
        return Result.ok();
    }

    /**
     * 修改站内信模板
     */
    @PutMapping
    @RequirePermission("system:notify:template:edit")
    @Log(title = "站内信模板", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody NotifyTemplateDTO notifyTemplateDTO) {
        notifyTemplateService.updateNotifyTemplate(notifyTemplateDTO);
        return Result.ok();
    }

    /**
     * 删除站内信模板
     */
    @DeleteMapping("/{id}")
    @RequirePermission("system:notify:template:remove")
    @Log(title = "站内信模板", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long id) {
        notifyTemplateService.deleteNotifyTemplate(id);
        return Result.ok();
    }
}
