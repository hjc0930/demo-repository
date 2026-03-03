package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysSmsChannel;
import com.soybean.admin.system.service.SysSmsChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短信渠道控制器
 */
@RestController
@RequestMapping("/api/system/sms/channel")
@RequiredArgsConstructor
public class SysSmsChannelController {

    private final SysSmsChannelService smsChannelService;

    /**
     * 分页查询短信渠道
     */
    @GetMapping("/page")
    @RequirePermission("system:sms:channel:list")
    public Result<IPage<SysSmsChannel>> page(Page<SysSmsChannel> page, String keyword) {
        IPage<SysSmsChannel> result = smsChannelService.selectSmsChannelPage(page, keyword);
        return Result.ok(result);
    }

    /**
     * 查询短信渠道列表
     */
    @GetMapping("/list")
    @RequirePermission("system:sms:channel:list")
    public Result<List<SysSmsChannel>> list(String status) {
        List<SysSmsChannel> list = smsChannelService.selectSmsChannelList(status);
        return Result.ok(list);
    }

    /**
     * 根据ID查询短信渠道
     */
    @GetMapping("/{id}")
    @RequirePermission("system:sms:channel:query")
    public Result<SysSmsChannel> getSmsChannel(@PathVariable Long id) {
        SysSmsChannel channel = smsChannelService.selectSmsChannelById(id);
        return Result.ok(channel);
    }

    /**
     * 根据编码查询短信渠道
     */
    @GetMapping("/code/{code}")
    @RequirePermission("system:sms:channel:query")
    public Result<SysSmsChannel> getSmsChannelByCode(@PathVariable String code) {
        SysSmsChannel channel = smsChannelService.selectSmsChannelByCode(code);
        return Result.ok(channel);
    }
}
