package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysMailAccount;
import com.soybean.admin.data.mapper.SysMailAccountMapper;
import com.soybean.admin.system.dto.MailAccountDTO;
import com.soybean.admin.system.service.SysMailAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 邮箱账号服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMailAccountServiceImpl extends ServiceImpl<SysMailAccountMapper, SysMailAccount> implements SysMailAccountService {

    private final SysMailAccountMapper mailAccountMapper;

    @Override
    public IPage<SysMailAccount> selectMailAccountPage(Page<SysMailAccount> page, String keyword) {
        LambdaQueryWrapper<SysMailAccount> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(SysMailAccount::getMail, keyword)
                    .or().like(SysMailAccount::getUsername, keyword)
                    .or().like(SysMailAccount::getHost, keyword));
        }

        wrapper.eq(SysMailAccount::getDelFlag, "0");
        wrapper.orderByDesc(SysMailAccount::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysMailAccount> selectMailAccountList(String status) {
        LambdaQueryWrapper<SysMailAccount> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(status)) {
            wrapper.eq(SysMailAccount::getStatus, status);
        }

        wrapper.eq(SysMailAccount::getDelFlag, "0");
        wrapper.orderByDesc(SysMailAccount::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysMailAccount selectMailAccountById(Long id) {
        return this.getById(id);
    }

    @Override
    public SysMailAccount selectMailAccountByMail(String mail) {
        LambdaQueryWrapper<SysMailAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMailAccount::getMail, mail);
        wrapper.eq(SysMailAccount::getDelFlag, "0");
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertMailAccount(MailAccountDTO mailAccountDTO) {
        // 检查邮箱是否已存在
        SysMailAccount existing = selectMailAccountByMail(mailAccountDTO.getMail());
        if (existing != null) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "邮箱地址已存在");
        }

        SysMailAccount account = new SysMailAccount();
        BeanUtils.copyProperties(mailAccountDTO, account);
        account.setDelFlag("0");

        return this.save(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMailAccount(MailAccountDTO mailAccountDTO) {
        SysMailAccount existing = this.getById(mailAccountDTO.getId());
        if (existing == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 检查邮箱是否被其他账号使用
        if (!existing.getMail().equals(mailAccountDTO.getMail())) {
            SysMailAccount duplicate = selectMailAccountByMail(mailAccountDTO.getMail());
            if (duplicate != null && !duplicate.getId().equals(mailAccountDTO.getId())) {
                throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "邮箱地址已存在");
            }
        }

        SysMailAccount account = new SysMailAccount();
        BeanUtils.copyProperties(mailAccountDTO, account);

        return this.updateById(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMailAccount(Long id) {
        // 逻辑删除
        SysMailAccount account = new SysMailAccount();
        account.setId(id);
        account.setDelFlag("2");

        return this.updateById(account);
    }
}
