package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysSmsChannel;
import com.soybean.admin.data.mapper.SysSmsChannelMapper;
import com.soybean.admin.system.service.SysSmsChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 短信渠道服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysSmsChannelServiceImpl extends ServiceImpl<SysSmsChannelMapper, SysSmsChannel> implements SysSmsChannelService {

    private final SysSmsChannelMapper smsChannelMapper;

    @Override
    public IPage<SysSmsChannel> selectSmsChannelPage(Page<SysSmsChannel> page, String keyword) {
        LambdaQueryWrapper<SysSmsChannel> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(SysSmsChannel::getName, keyword)
                    .or().like(SysSmsChannel::getCode, keyword));
        }

        wrapper.eq(SysSmsChannel::getDelFlag, "0");
        wrapper.orderByDesc(SysSmsChannel::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysSmsChannel> selectSmsChannelList(String status) {
        LambdaQueryWrapper<SysSmsChannel> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(status)) {
            wrapper.eq(SysSmsChannel::getStatus, status);
        }

        wrapper.eq(SysSmsChannel::getDelFlag, "0");
        wrapper.orderByDesc(SysSmsChannel::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysSmsChannel selectSmsChannelById(Long id) {
        return this.getById(id);
    }

    @Override
    public SysSmsChannel selectSmsChannelByCode(String code) {
        LambdaQueryWrapper<SysSmsChannel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysSmsChannel::getCode, code);
        wrapper.eq(SysSmsChannel::getDelFlag, "0");
        return this.getOne(wrapper);
    }
}
