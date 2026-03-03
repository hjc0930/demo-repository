package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysNotice;
import com.soybean.admin.system.dto.NoticeDTO;

import java.util.List;

/**
 * 通知公告服务接口
 */
public interface SysNoticeService extends IService<SysNotice> {

    /**
     * 分页查询通知公告
     */
    IPage<SysNotice> selectNoticePage(Page<SysNotice> page, NoticeDTO query);

    /**
     * 查询通知公告列表
     */
    List<SysNotice> selectNoticeList(NoticeDTO query);

    /**
     * 根据ID查询通知公告
     */
    SysNotice selectNoticeById(Long noticeId);

    /**
     * 新增通知公告
     */
    boolean insertNotice(NoticeDTO noticeDTO);

    /**
     * 修改通知公告
     */
    boolean updateNotice(NoticeDTO noticeDTO);

    /**
     * 删除通知公告
     */
    boolean deleteNotice(Long noticeId);
}
