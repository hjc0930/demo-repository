package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysUser;
import com.soybean.admin.data.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 用户服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    private final PasswordEncoder passwordEncoder;

    /**
     * 分页查询用户列表
     */
    public Result<IPage<SysUser>> selectUserPage(Page<SysUser> page, SysUser query) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getUserName())) {
            wrapper.like(SysUser::getUserName, query.getUserName());
        }
        if (StringUtils.hasText(query.getNickName())) {
            wrapper.like(SysUser::getNickName, query.getNickName());
        }
        if (StringUtils.hasText(query.getPhonenumber())) {
            wrapper.like(SysUser::getPhonenumber, query.getPhonenumber());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysUser::getStatus, query.getStatus());
        }
        if (query.getDeptId() != null) {
            wrapper.eq(SysUser::getDeptId, query.getDeptId());
        }

        wrapper.orderByDesc(SysUser::getCreateTime);

        IPage<SysUser> userPage = this.page(page, wrapper);
        return Result.page(userPage.getRecords(), userPage.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 根据用户ID查询用户
     */
    public SysUser selectUserById(Long userId) {
        return this.getById(userId);
    }

    /**
     * 新增用户
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(SysUser user) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserName, user.getUserName());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ResponseCode.USER_ALREADY_EXISTS);
        }

        // 加密密码
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode("123456")); // 默认密码
        }

        return this.save(user);
    }

    /**
     * 更新用户
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        SysUser existingUser = this.getById(user.getUserId());
        if (existingUser == null) {
            throw new BusinessException(ResponseCode.USER_NOT_FOUND);
        }

        // 检查用户名是否被其他用户使用
        if (!existingUser.getUserName().equals(user.getUserName())) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getUserName, user.getUserName());
            wrapper.ne(SysUser::getUserId, user.getUserId());
            if (this.count(wrapper) > 0) {
                throw new BusinessException(ResponseCode.USER_ALREADY_EXISTS);
            }
        }

        // 如果修改了密码，需要加密
        if (StringUtils.hasText(user.getPassword()) && !user.getPassword().equals(existingUser.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }

        return this.updateById(user);
    }

    /**
     * 删除用户
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long userId) {
        return this.removeById(userId);
    }

    /**
     * 重置用户密码
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPassword(Long userId, String newPassword) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        return this.updateById(user);
    }

    /**
     * 修改用户状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserStatus(Long userId, String status) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setStatus(status);
        return this.updateById(user);
    }
}
