package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysSmsTemplate;
import com.soybean.admin.system.dto.SmsTemplateDTO;
import com.soybean.admin.system.service.SysSmsTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短信模板控制器
 */
@RestController
@RequestMapping("/api/system/sms/template")
@RequiredArgsConstructor
public class SysSmsTemplateController {

    private final SysSmsTemplateService smsTemplateService;

    /**
     * 分页查询短信模板
     */
    @GetMapping("/page")
    @RequirePermission("system:sms:template:list")
    public Result<IPage<SysSmsTemplate>> page(Page<SysSmsTemplate> page, SmsTemplateDTO query) {
        IPage<SysSmsTemplate> result = smsTemplateService.selectSmsTemplatePage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询短信模板列表
     */
    @GetMapping("/list")
    @RequirePermission("system:sms:template:list")
    public Result<List<SysSmsTemplate>> list(SmsTemplateDTO query) {
        List<SysSmsTemplate> list = smsTemplateService.selectSmsTemplateList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询短信模板
     */
    @GetMapping("/{id}")
    @RequirePermission("system:sms:template:query")
    public Result<SysSmsTemplate> getSmsTemplate(@PathVariable Long id) {
        SysSmsTemplate template = smsTemplateService.selectSmsTemplateById(id);
        return Result.ok(template);
    }

    /**
     * 根据编码查询短信模板
     */
    @GetMapping("/code/{code}")
    @RequirePermission("system:sms:template:query")
    public Result<SysSmsTemplate> getSmsTemplateByCode(@PathVariable String code) {
        SysSmsTemplate template = smsTemplateService.selectSmsTemplateByCode(code);
        return Result.ok(template);
    }

    /**
     * 新增短信模板
     */
    @PostMapping
    @RequirePermission("system:sms:template:add")
    @Log(title = "短信模板", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody SmsTemplateDTO smsTemplateDTO) {
        smsTemplateService.insertSmsTemplate(smsTemplateDTO);
        return Result.ok();
    }

    /**
     * 修改短信模板
     */
    @PutMapping
    @RequirePermission("system:sms:template:edit")
    @Log(title = "短信模板", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody SmsTemplateDTO smsTemplateDTO) {
        smsTemplateService.updateSmsTemplate(smsTemplateDTO);
        return Result.ok();
    }

    /**
     * 删除短信模板
     */
    @DeleteMapping("/{id}")
    @RequirePermission("system:sms:template:remove")
    @Log(title = "短信模板", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long id) {
        smsTemplateService.deleteSmsTemplate(id);
        return Result.ok();
    }
}
