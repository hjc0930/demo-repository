package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysNotifyMessage;
import com.soybean.admin.data.mapper.SysNotifyMessageMapper;
import com.soybean.admin.system.dto.NotifyMessageDTO;
import com.soybean.admin.system.service.SysNotifyMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 站内信消息服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysNotifyMessageServiceImpl extends ServiceImpl<SysNotifyMessageMapper, SysNotifyMessage> implements SysNotifyMessageService {

    private final SysNotifyMessageMapper notifyMessageMapper;

    @Override
    public IPage<SysNotifyMessage> selectNotifyMessagePage(Page<SysNotifyMessage> page, NotifyMessageDTO query) {
        LambdaQueryWrapper<SysNotifyMessage> wrapper = new LambdaQueryWrapper<>();

        if (query.getUserId() != null) {
            wrapper.eq(SysNotifyMessage::getUserId, query.getUserId());
        }
        if (query.getReadStatus() != null) {
            wrapper.eq(SysNotifyMessage::getReadStatus, query.getReadStatus());
        }

        wrapper.eq(SysNotifyMessage::getDelFlag, "0");
        wrapper.orderByDesc(SysNotifyMessage::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysNotifyMessage> selectNotifyMessageList(NotifyMessageDTO query) {
        LambdaQueryWrapper<SysNotifyMessage> wrapper = new LambdaQueryWrapper<>();

        if (query.getUserId() != null) {
            wrapper.eq(SysNotifyMessage::getUserId, query.getUserId());
        }
        if (query.getReadStatus() != null) {
            wrapper.eq(SysNotifyMessage::getReadStatus, query.getReadStatus());
        }

        wrapper.eq(SysNotifyMessage::getDelFlag, "0");
        wrapper.orderByDesc(SysNotifyMessage::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysNotifyMessage selectNotifyMessageById(Long id) {
        return this.getById(id);
    }

    @Override
    public Long selectUnreadCount(Long userId) {
        LambdaQueryWrapper<SysNotifyMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysNotifyMessage::getUserId, userId);
        wrapper.eq(SysNotifyMessage::getReadStatus, false);
        wrapper.eq(SysNotifyMessage::getDelFlag, "0");
        return this.count(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long id) {
        SysNotifyMessage message = new SysNotifyMessage();
        message.setId(id);
        message.setReadStatus(true);

        return this.updateById(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAllAsRead(Long userId) {
        LambdaQueryWrapper<SysNotifyMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysNotifyMessage::getUserId, userId);
        wrapper.eq(SysNotifyMessage::getReadStatus, false);

        SysNotifyMessage message = new SysNotifyMessage();
        message.setReadStatus(true);

        return this.update(message, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNotifyMessage(Long id) {
        SysNotifyMessage message = new SysNotifyMessage();
        message.setId(id);
        message.setDelFlag("2");

        return this.updateById(message);
    }
}
