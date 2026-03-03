package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysSmsLog;
import com.soybean.admin.data.mapper.SysSmsLogMapper;
import com.soybean.admin.system.dto.SmsLogDTO;
import com.soybean.admin.system.service.SysSmsLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 短信日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysSmsLogServiceImpl extends ServiceImpl<SysSmsLogMapper, SysSmsLog> implements SysSmsLogService {

    private final SysSmsLogMapper smsLogMapper;

    @Override
    public IPage<SysSmsLog> selectSmsLogPage(Page<SysSmsLog> page, SmsLogDTO query) {
        LambdaQueryWrapper<SysSmsLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getMobile())) {
            wrapper.eq(SysSmsLog::getMobile, query.getMobile());
        }
        if (StringUtils.hasText(query.getTemplateCode())) {
            wrapper.eq(SysSmsLog::getTemplateCode, query.getTemplateCode());
        }
        if (query.getSendStatus() != null) {
            wrapper.eq(SysSmsLog::getSendStatus, query.getSendStatus());
        }
        if (StringUtils.hasText(query.getParams())) {
            // 处理时间范围查询
            String[] params = query.getParams().split(",");
            if (params.length == 2) {
                wrapper.ge(SysSmsLog::getCreateTime, params[0]);
                wrapper.le(SysSmsLog::getCreateTime, params[1]);
            }
        }

        wrapper.orderByDesc(SysSmsLog::getId);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysSmsLog> selectSmsLogList(SmsLogDTO query) {
        LambdaQueryWrapper<SysSmsLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getMobile())) {
            wrapper.eq(SysSmsLog::getMobile, query.getMobile());
        }
        if (StringUtils.hasText(query.getTemplateCode())) {
            wrapper.eq(SysSmsLog::getTemplateCode, query.getTemplateCode());
        }
        if (query.getSendStatus() != null) {
            wrapper.eq(SysSmsLog::getSendStatus, query.getSendStatus());
        }

        wrapper.orderByDesc(SysSmsLog::getId);
        return this.list(wrapper);
    }

    @Override
    public SysSmsLog selectSmsLogById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSmsLog(SmsLogDTO smsLogDTO) {
        SysSmsLog smsLog = new SysSmsLog();
        BeanUtils.copyProperties(smsLogDTO, smsLog);

        return this.save(smsLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSmsLogByIds(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        return this.removeByIds(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanSmsLog() {
        return this.remove(new LambdaQueryWrapper<>());
    }
}
