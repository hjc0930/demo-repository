package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysNotifyMessage;
import com.soybean.admin.system.dto.NotifyMessageDTO;

import java.util.List;

/**
 * 站内信消息服务接口
 */
public interface SysNotifyMessageService extends IService<SysNotifyMessage> {

    /**
     * 分页查询站内信消息
     */
    IPage<SysNotifyMessage> selectNotifyMessagePage(Page<SysNotifyMessage> page, NotifyMessageDTO query);

    /**
     * 查询站内信消息列表
     */
    List<SysNotifyMessage> selectNotifyMessageList(NotifyMessageDTO query);

    /**
     * 根据ID查询站内信消息
     */
    SysNotifyMessage selectNotifyMessageById(Long id);

    /**
     * 查询用户未读消息数量
     */
    Long selectUnreadCount(Long userId);

    /**
     * 标记消息为已读
     */
    boolean markAsRead(Long id);

    /**
     * 批量标记消息为已读
     */
    boolean markAllAsRead(Long userId);

    /**
     * 删除站内信消息
     */
    boolean deleteNotifyMessage(Long id);
}
