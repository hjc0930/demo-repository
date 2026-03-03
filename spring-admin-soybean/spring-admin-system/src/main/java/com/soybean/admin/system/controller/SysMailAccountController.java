package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysMailAccount;
import com.soybean.admin.system.dto.MailAccountDTO;
import com.soybean.admin.system.service.SysMailAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮箱账号控制器
 */
@RestController
@RequestMapping("/api/system/mail/account")
@RequiredArgsConstructor
public class SysMailAccountController {

    private final SysMailAccountService mailAccountService;

    /**
     * 分页查询邮箱账号
     */
    @GetMapping("/page")
    @RequirePermission("system:mail:account:list")
    public Result<IPage<SysMailAccount>> page(Page<SysMailAccount> page, String keyword) {
        IPage<SysMailAccount> result = mailAccountService.selectMailAccountPage(page, keyword);
        return Result.ok(result);
    }

    /**
     * 查询邮箱账号列表
     */
    @GetMapping("/list")
    @RequirePermission("system:mail:account:list")
    public Result<List<SysMailAccount>> list(String status) {
        List<SysMailAccount> list = mailAccountService.selectMailAccountList(status);
        return Result.ok(list);
    }

    /**
     * 根据ID查询邮箱账号
     */
    @GetMapping("/{id}")
    @RequirePermission("system:mail:account:query")
    public Result<SysMailAccount> getMailAccount(@PathVariable Long id) {
        SysMailAccount account = mailAccountService.selectMailAccountById(id);
        return Result.ok(account);
    }

    /**
     * 新增邮箱账号
     */
    @PostMapping
    @RequirePermission("system:mail:account:add")
    @Log(title = "邮箱账号", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody MailAccountDTO mailAccountDTO) {
        mailAccountService.insertMailAccount(mailAccountDTO);
        return Result.ok();
    }

    /**
     * 修改邮箱账号
     */
    @PutMapping
    @RequirePermission("system:mail:account:edit")
    @Log(title = "邮箱账号", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody MailAccountDTO mailAccountDTO) {
        mailAccountService.updateMailAccount(mailAccountDTO);
        return Result.ok();
    }

    /**
     * 删除邮箱账号
     */
    @DeleteMapping("/{id}")
    @RequirePermission("system:mail:account:remove")
    @Log(title = "邮箱账号", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long id) {
        mailAccountService.deleteMailAccount(id);
        return Result.ok();
    }
}
