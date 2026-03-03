package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysSmsChannel;

import java.util.List;

/**
 * 短信渠道服务接口
 */
public interface SysSmsChannelService extends IService<SysSmsChannel> {

    /**
     * 分页查询短信渠道
     */
    IPage<SysSmsChannel> selectSmsChannelPage(Page<SysSmsChannel> page, String keyword);

    /**
     * 查询短信渠道列表
     */
    List<SysSmsChannel> selectSmsChannelList(String status);

    /**
     * 根据ID查询短信渠道
     */
    SysSmsChannel selectSmsChannelById(Long id);

    /**
     * 根据编码查询短信渠道
     */
    SysSmsChannel selectSmsChannelByCode(String code);
}
