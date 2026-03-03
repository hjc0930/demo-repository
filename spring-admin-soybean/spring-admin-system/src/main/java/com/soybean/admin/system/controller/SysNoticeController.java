package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysNotice;
import com.soybean.admin.system.dto.NoticeDTO;
import com.soybean.admin.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告控制器
 */
@RestController
@RequestMapping("/api/system/notice")
@RequiredArgsConstructor
public class SysNoticeController {

    private final SysNoticeService noticeService;

    /**
     * 分页查询通知公告
     */
    @GetMapping("/page")
    @RequirePermission("system:notice:list")
    public Result<IPage<SysNotice>> page(Page<SysNotice> page, NoticeDTO query) {
        IPage<SysNotice> result = noticeService.selectNoticePage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询通知公告列表
     */
    @GetMapping("/list")
    @RequirePermission("system:notice:list")
    public Result<List<SysNotice>> list(NoticeDTO query) {
        List<SysNotice> list = noticeService.selectNoticeList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询通知公告
     */
    @GetMapping("/{noticeId}")
    @RequirePermission("system:notice:query")
    public Result<SysNotice> getNotice(@PathVariable Long noticeId) {
        SysNotice notice = noticeService.selectNoticeById(noticeId);
        return Result.ok(notice);
    }

    /**
     * 新增通知公告
     */
    @PostMapping
    @RequirePermission("system:notice:add")
    @Log(title = "通知公告", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody NoticeDTO noticeDTO) {
        noticeService.insertNotice(noticeDTO);
        return Result.ok();
    }

    /**
     * 修改通知公告
     */
    @PutMapping
    @RequirePermission("system:notice:edit")
    @Log(title = "通知公告", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody NoticeDTO noticeDTO) {
        noticeService.updateNotice(noticeDTO);
        return Result.ok();
    }

    /**
     * 删除通知公告
     */
    @DeleteMapping("/{noticeId}")
    @RequirePermission("system:notice:remove")
    @Log(title = "通知公告", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return Result.ok();
    }
}
