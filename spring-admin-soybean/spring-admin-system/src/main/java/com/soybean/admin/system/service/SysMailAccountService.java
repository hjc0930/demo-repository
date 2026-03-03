package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysMailAccount;
import com.soybean.admin.system.dto.MailAccountDTO;

import java.util.List;

/**
 * 邮箱账号服务接口
 */
public interface SysMailAccountService extends IService<SysMailAccount> {

    /**
     * 分页查询邮箱账号
     */
    IPage<SysMailAccount> selectMailAccountPage(Page<SysMailAccount> page, String keyword);

    /**
     * 查询邮箱账号列表
     */
    List<SysMailAccount> selectMailAccountList(String status);

    /**
     * 根据ID查询邮箱账号
     */
    SysMailAccount selectMailAccountById(Long id);

    /**
     * 根据邮箱查询账号
     */
    SysMailAccount selectMailAccountByMail(String mail);

    /**
     * 新增邮箱账号
     */
    boolean insertMailAccount(MailAccountDTO mailAccountDTO);

    /**
     * 修改邮箱账号
     */
    boolean updateMailAccount(MailAccountDTO mailAccountDTO);

    /**
     * 删除邮箱账号
     */
    boolean deleteMailAccount(Long id);
}
