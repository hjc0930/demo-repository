package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysMailLog;
import com.soybean.admin.data.mapper.SysMailLogMapper;
import com.soybean.admin.system.dto.MailLogDTO;
import com.soybean.admin.system.service.SysMailLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 邮件日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMailLogServiceImpl extends ServiceImpl<SysMailLogMapper, SysMailLog> implements SysMailLogService {

    private final SysMailLogMapper mailLogMapper;

    @Override
    public IPage<SysMailLog> selectMailLogPage(Page<SysMailLog> page, MailLogDTO query) {
        LambdaQueryWrapper<SysMailLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getToMail())) {
            wrapper.like(SysMailLog::getToMail, query.getToMail());
        }
        if (StringUtils.hasText(query.getTemplateCode())) {
            wrapper.eq(SysMailLog::getTemplateCode, query.getTemplateCode());
        }
        if (query.getSendStatus() != null) {
            wrapper.eq(SysMailLog::getSendStatus, query.getSendStatus());
        }
        if (StringUtils.hasText(query.getParams())) {
            // 处理时间范围查询
            String[] params = query.getParams().split(",");
            if (params.length == 2) {
                wrapper.ge(SysMailLog::getCreateTime, params[0]);
                wrapper.le(SysMailLog::getCreateTime, params[1]);
            }
        }

        wrapper.orderByDesc(SysMailLog::getId);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysMailLog> selectMailLogList(MailLogDTO query) {
        LambdaQueryWrapper<SysMailLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getToMail())) {
            wrapper.like(SysMailLog::getToMail, query.getToMail());
        }
        if (StringUtils.hasText(query.getTemplateCode())) {
            wrapper.eq(SysMailLog::getTemplateCode, query.getTemplateCode());
        }
        if (query.getSendStatus() != null) {
            wrapper.eq(SysMailLog::getSendStatus, query.getSendStatus());
        }

        wrapper.orderByDesc(SysMailLog::getId);
        return this.list(wrapper);
    }

    @Override
    public SysMailLog selectMailLogById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertMailLog(MailLogDTO mailLogDTO) {
        SysMailLog mailLog = new SysMailLog();
        BeanUtils.copyProperties(mailLogDTO, mailLog);

        return this.save(mailLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMailLogByIds(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        return this.removeByIds(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanMailLog() {
        return this.remove(new LambdaQueryWrapper<>());
    }
}
