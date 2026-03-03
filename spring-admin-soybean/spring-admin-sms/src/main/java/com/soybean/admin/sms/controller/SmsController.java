package com.soybean.admin.sms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.response.PageResult;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysSmsChannel;
import com.soybean.admin.data.entity.SysSmsLog;
import com.soybean.admin.data.entity.SysSmsTemplate;
import com.soybean.admin.data.mapper.SysSmsChannelMapper;
import com.soybean.admin.data.mapper.SysSmsLogMapper;
import com.soybean.admin.data.mapper.SysSmsTemplateMapper;
import com.soybean.admin.sms.dto.SmsSendDTO;
import com.soybean.admin.sms.service.SmsSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 短信管理控制器
 */
@RestController
@RequestMapping("/api/system/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsSendService smsSendService;
    private final SysSmsChannelMapper smsChannelMapper;
    private final SysSmsTemplateMapper smsTemplateMapper;
    private final SysSmsLogMapper smsLogMapper;

    // ==================== 短信渠道管理 ====================

    /**
     * 分页查询短信渠道列表
     */
    @GetMapping("/channel/list")
    public Result<PageResult<SysSmsChannel>> listChannel(
            @RequestParam(required = false) String channelName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SysSmsChannel> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysSmsChannel> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(channelName)) {
            wrapper.like(SysSmsChannel::getChannelName, channelName);
        }

        wrapper.orderByDesc(SysSmsChannel::getCreateTime);
        IPage<SysSmsChannel> result = smsChannelMapper.selectPage(page, wrapper);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取短信渠道详情
     */
    @GetMapping("/channel/{id}")
    public Result<SysSmsChannel> getChannel(@PathVariable Long id) {
        SysSmsChannel channel = smsChannelMapper.selectById(id);
        if (channel != null) {
            // 隐藏密钥
            channel.setAccessKey(maskSensitive(channel.getAccessKey()));
            channel.setSecretKey(maskSensitive(channel.getSecretKey()));
        }
        return Result.success(channel);
    }

    /**
     * 新增短信渠道
     */
    @PostMapping("/channel")
    public Result<Void> addChannel(@RequestBody SysSmsChannel channel) {
        smsChannelMapper.insert(channel);
        return Result.success();
    }

    /**
     * 修改短信渠道
     */
    @PutMapping("/channel")
    public Result<Void> updateChannel(@RequestBody SysSmsChannel channel) {
        smsChannelMapper.updateById(channel);
        return Result.success();
    }

    /**
     * 删除短信渠道
     */
    @DeleteMapping("/channel/{id}")
    public Result<Void> deleteChannel(@PathVariable Long id) {
        smsChannelMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 设置默认渠道
     */
    @PutMapping("/channel/{id}/default")
    public Result<Void> setDefault(@PathVariable Long id) {
        // 取消其他默认渠道
        smsChannelMapper.update(null,
            new LambdaQueryWrapper<SysSmsChannel>()
                .set(SysSmsChannel::getIsDefault, "0")
        );

        // 设置当前为默认
        SysSmsChannel channel = new SysSmsChannel();
        channel.setChannelId(id);
        channel.setIsDefault("1");
        smsChannelMapper.updateById(channel);

        return Result.success();
    }

    // ==================== 短信模板管理 ====================

    /**
     * 分页查询短信模板列表
     */
    @GetMapping("/template/list")
    public Result<PageResult<SysSmsTemplate>> listTemplate(
            @RequestParam(required = false) String templateName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SysSmsTemplate> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysSmsTemplate> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(templateName)) {
            wrapper.like(SysSmsTemplate::getTemplateName, templateName);
        }

        wrapper.orderByDesc(SysSmsTemplate::getCreateTime);
        IPage<SysSmsTemplate> result = smsTemplateMapper.selectPage(page, wrapper);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取短信模板详情
     */
    @GetMapping("/template/{id}")
    public Result<SysSmsTemplate> getTemplate(@PathVariable Long id) {
        return Result.success(smsTemplateMapper.selectById(id));
    }

    /**
     * 新增短信模板
     */
    @PostMapping("/template")
    public Result<Void> addTemplate(@RequestBody SysSmsTemplate template) {
        smsTemplateMapper.insert(template);
        return Result.success();
    }

    /**
     * 修改短信模板
     */
    @PutMapping("/template")
    public Result<Void> updateTemplate(@RequestBody SysSmsTemplate template) {
        smsTemplateMapper.updateById(template);
        return Result.success();
    }

    /**
     * 删除短信模板
     */
    @DeleteMapping("/template/{id}")
    public Result<Void> deleteTemplate(@PathVariable Long id) {
        smsTemplateMapper.deleteById(id);
        return Result.success();
    }

    // ==================== 短信发送 ====================

    /**
     * 发送短信
     */
    @PostMapping("/send")
    public Result<Void> sendSms(@RequestBody SmsSendDTO smsSendDTO) {
        smsSendService.sendAsyncSms(smsSendDTO);
        return Result.success();
    }

    /**
     * 发送验证码
     */
    @PostMapping("/send-verify-code")
    public Result<Void> sendVerifyCode(@RequestParam String phone) {
        smsSendService.sendVerifyCode(phone, null);
        return Result.success();
    }

    /**
     * 验证验证码
     */
    @PostMapping("/verify-code")
    public Result<Boolean> verifyCode(@RequestParam String phone, @RequestParam String code) {
        boolean result = smsSendService.verifyVerifyCode(phone, code);
        return Result.success(result);
    }

    // ==================== 短信日志 ====================

    /**
     * 分页查询短信日志列表
     */
    @GetMapping("/log/list")
    public Result<PageResult<SysSmsLog>> listLog(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<SysSmsLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysSmsLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(phone)) {
            wrapper.like(SysSmsLog::getPhone, phone);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(SysSmsLog::getStatus, status);
        }

        wrapper.orderByDesc(SysSmsLog::getSendTime);
        IPage<SysSmsLog> result = smsLogMapper.selectPage(page, wrapper);

        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取短信日志详情
     */
    @GetMapping("/log/{id}")
    public Result<SysSmsLog> getLog(@PathVariable Long id) {
        return Result.success(smsLogMapper.selectById(id));
    }

    private String maskSensitive(String value) {
        if (value == null || value.length() <= 8) {
            return "******";
        }
        return value.substring(0, 4) + "******" + value.substring(value.length() - 4);
    }
}
