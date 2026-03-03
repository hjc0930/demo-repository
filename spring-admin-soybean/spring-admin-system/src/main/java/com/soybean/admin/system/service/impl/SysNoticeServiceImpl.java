package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysNotice;
import com.soybean.admin.data.mapper.SysNoticeMapper;
import com.soybean.admin.system.dto.NoticeDTO;
import com.soybean.admin.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 通知公告服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    private final SysNoticeMapper noticeMapper;

    @Override
    public IPage<SysNotice> selectNoticePage(Page<SysNotice> page, NoticeDTO query) {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getNoticeTitle())) {
            wrapper.like(SysNotice::getNoticeTitle, query.getNoticeTitle());
        }
        if (StringUtils.hasText(query.getNoticeType())) {
            wrapper.eq(SysNotice::getNoticeType, query.getNoticeType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysNotice::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.like(SysNotice::getNoticeTitle, query.getKeyword());
        }

        // 查询未删除的数据
        wrapper.eq(SysNotice::getDelFlag, "0");
        wrapper.orderByDesc(SysNotice::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysNotice> selectNoticeList(NoticeDTO query) {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getNoticeTitle())) {
            wrapper.like(SysNotice::getNoticeTitle, query.getNoticeTitle());
        }
        if (StringUtils.hasText(query.getNoticeType())) {
            wrapper.eq(SysNotice::getNoticeType, query.getNoticeType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysNotice::getStatus, query.getStatus());
        }

        wrapper.eq(SysNotice::getDelFlag, "0");
        wrapper.orderByDesc(SysNotice::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysNotice selectNoticeById(Long noticeId) {
        return this.getById(noticeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertNotice(NoticeDTO noticeDTO) {
        SysNotice notice = new SysNotice();
        BeanUtils.copyProperties(noticeDTO, notice);
        notice.setDelFlag("0");

        return this.save(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNotice(NoticeDTO noticeDTO) {
        SysNotice notice = new SysNotice();
        BeanUtils.copyProperties(noticeDTO, notice);

        return this.updateById(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNotice(Long noticeId) {
        // 逻辑删除
        SysNotice notice = new SysNotice();
        notice.setNoticeId(noticeId);
        notice.setDelFlag("2");

        return this.updateById(notice);
    }
}
