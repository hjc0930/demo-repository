package com.soybean.admin.mail.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.response.PageResult;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysMailAccount;
import com.soybean.admin.data.entity.SysMailLog;
import com.soybean.admin.data.entity.SysMailTemplate;
import com.soybean.admin.data.mapper.SysMailAccountMapper;
import com.soybean.admin.data.mapper.SysMailLogMapper;
import com.soybean.admin.data.mapper.SysMailTemplateMapper;
import com.soybean.admin.mail.dto.MailSendDTO;
import com.soybean.admin.mail.service.MailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 邮件管理控制器
 */
@RestController
@RequestMapping("/api/system/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailSendService mailSendService;
    private final SysMailAccountMapper mailAccountMapper;
    private final SysMailTemplateMapper mailTemplateMapper;
    private final SysMailLogMapper mailLogMapper;

    // ==================== 邮箱账号管理 ====================

    /**
     * 分页查询邮箱账号列表
     */
    @GetMapping("/account/list")
    public Result<PageResult<SysMailAccount>> listAccount(
            @RequestParam(required = false) String accountName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SysMailAccount> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysMailAccount> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(accountName)) {
            wrapper.like(SysMailAccount::getAccountName, accountName);
        }

        wrapper.orderByDesc(SysMailAccount::getCreateTime);
        IPage<SysMailAccount> result = mailAccountMapper.selectPage(page, wrapper);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取邮箱账号详情
     */
    @GetMapping("/account/{id}")
    public Result<SysMailAccount> getAccount(@PathVariable Long id) {
        SysMailAccount account = mailAccountMapper.selectById(id);
        if (account != null) {
            // 隐藏密码
            account.setPassword("******");
        }
        return Result.success(account);
    }

    /**
     * 新增邮箱账号
     */
    @PostMapping("/account")
    public Result<Void> addAccount(@RequestBody SysMailAccount account) {
        mailAccountMapper.insert(account);
        return Result.success();
    }

    /**
     * 修改邮箱账号
     */
    @PutMapping("/account")
    public Result<Void> updateAccount(@RequestBody SysMailAccount account) {
        mailAccountMapper.updateById(account);
        return Result.success();
    }

    /**
     * 删除邮箱账号
     */
    @DeleteMapping("/account/{id}")
    public Result<Void> deleteAccount(@PathVariable Long id) {
        mailAccountMapper.deleteById(id);
        return Result.success();
    }

    // ==================== 邮件模板管理 ====================

    /**
     * 分页查询邮件模板列表
     */
    @GetMapping("/template/list")
    public Result<PageResult<SysMailTemplate>> listTemplate(
            @RequestParam(required = false) String templateName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SysMailTemplate> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysMailTemplate> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(templateName)) {
            wrapper.like(SysMailTemplate::getTemplateName, templateName);
        }

        wrapper.orderByDesc(SysMailTemplate::getCreateTime);
        IPage<SysMailTemplate> result = mailTemplateMapper.selectPage(page, wrapper);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取邮件模板详情
     */
    @GetMapping("/template/{id}")
    public Result<SysMailTemplate> getTemplate(@PathVariable Long id) {
        return Result.success(mailTemplateMapper.selectById(id));
    }

    /**
     * 新增邮件模板
     */
    @PostMapping("/template")
    public Result<Void> addTemplate(@RequestBody SysMailTemplate template) {
        mailTemplateMapper.insert(template);
        return Result.success();
    }

    /**
     * 修改邮件模板
     */
    @PutMapping("/template")
    public Result<Void> updateTemplate(@RequestBody SysMailTemplate template) {
        mailTemplateMapper.updateById(template);
        return Result.success();
    }

    /**
     * 删除邮件模板
     */
    @DeleteMapping("/template/{id}")
    public Result<Void> deleteTemplate(@PathVariable Long id) {
        mailTemplateMapper.deleteById(id);
        return Result.success();
    }

    // ==================== 邮件发送 ====================

    /**
     * 发送邮件
     */
    @PostMapping("/send")
    public Result<Void> sendMail(@RequestBody MailSendDTO mailSendDTO) {
        mailSendService.sendAsyncMail(mailSendDTO);
        return Result.success();
    }

    /**
     * 测试邮件连接
     */
    @PostMapping("/account/{id}/test")
    public Result<Boolean> testConnection(@PathVariable Long id) {
        // TODO: 实现测试连接逻辑
        return Result.success(true);
    }

    // ==================== 邮件日志 ====================

    /**
     * 分页查询邮件日志列表
     */
    @GetMapping("/log/list")
    public Result<PageResult<SysMailLog>> listLog(
            @RequestParam(required = false) String toAddress,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SysMailLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysMailLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(toAddress)) {
            wrapper.like(SysMailLog::getToAddress, toAddress);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(SysMailLog::getStatus, status);
        }

        wrapper.orderByDesc(SysMailLog::getSendTime);
        IPage<SysMailLog> result = mailLogMapper.selectPage(page, wrapper);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取邮件日志详情
     */
    @GetMapping("/log/{id}")
    public Result<SysMailLog> getLog(@PathVariable Long id) {
        return Result.success(mailLogMapper.selectById(id));
    }
}
