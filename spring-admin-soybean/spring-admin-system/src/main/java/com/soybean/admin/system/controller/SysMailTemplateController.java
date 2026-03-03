package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysMailTemplate;
import com.soybean.admin.system.dto.MailTemplateDTO;
import com.soybean.admin.system.service.SysMailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮件模板控制器
 */
@RestController
@RequestMapping("/api/system/mail/template")
@RequiredArgsConstructor
public class SysMailTemplateController {

    private final SysMailTemplateService mailTemplateService;

    /**
     * 分页查询邮件模板
     */
    @GetMapping("/page")
    @RequirePermission("system:mail:template:list")
    public Result<IPage<SysMailTemplate>> page(Page<SysMailTemplate> page, MailTemplateDTO query) {
        IPage<SysMailTemplate> result = mailTemplateService.selectMailTemplatePage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询邮件模板列表
     */
    @GetMapping("/list")
    @RequirePermission("system:mail:template:list")
    public Result<List<SysMailTemplate>> list(MailTemplateDTO query) {
        List<SysMailTemplate> list = mailTemplateService.selectMailTemplateList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询邮件模板
     */
    @GetMapping("/{id}")
    @RequirePermission("system:mail:template:query")
    public Result<SysMailTemplate> getMailTemplate(@PathVariable Long id) {
        SysMailTemplate template = mailTemplateService.selectMailTemplateById(id);
        return Result.ok(template);
    }

    /**
     * 根据编码查询邮件模板
     */
    @GetMapping("/code/{code}")
    @RequirePermission("system:mail:template:query")
    public Result<SysMailTemplate> getMailTemplateByCode(@PathVariable String code) {
        SysMailTemplate template = mailTemplateService.selectMailTemplateByCode(code);
        return Result.ok(template);
    }

    /**
     * 新增邮件模板
     */
    @PostMapping
    @RequirePermission("system:mail:template:add")
    @Log(title = "邮件模板", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody MailTemplateDTO mailTemplateDTO) {
        mailTemplateService.insertMailTemplate(mailTemplateDTO);
        return Result.ok();
    }

    /**
     * 修改邮件模板
     */
    @PutMapping
    @RequirePermission("system:mail:template:edit")
    @Log(title = "邮件模板", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody MailTemplateDTO mailTemplateDTO) {
        mailTemplateService.updateMailTemplate(mailTemplateDTO);
        return Result.ok();
    }

    /**
     * 删除邮件模板
     */
    @DeleteMapping("/{id}")
    @RequirePermission("system:mail:template:remove")
    @Log(title = "邮件模板", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long id) {
        mailTemplateService.deleteMailTemplate(id);
        return Result.ok();
    }
}
