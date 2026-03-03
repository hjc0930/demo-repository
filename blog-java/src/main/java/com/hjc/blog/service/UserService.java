package com.hjc.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjc.blog.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息，不存在返回null
     */
    User getByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户信息，不存在返回null
     */
    User getByEmail(String email);

    /**
     * 更新用户最后登录信息
     *
     * @param userId 用户ID
     * @param ip     登录IP
     */
    void updateLastLoginInfo(Long userId, String ip);

    /**
     * 修改用户密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码（已加密）
     * @return 是否成功
     */
    Boolean updatePassword(Long userId, String newPassword);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态：0-禁用，1-正常
     * @return 是否成功
     */
    Boolean updateStatus(Long userId, Integer status);
}
