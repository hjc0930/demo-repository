package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysOperLog;
import com.soybean.admin.data.mapper.SysOperLogMapper;
import com.soybean.admin.system.dto.OperLogDTO;
import com.soybean.admin.system.service.SysOperLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 操作日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {

    private final SysOperLogMapper operLogMapper;

    @Override
    public IPage<SysOperLog> selectOperLogPage(Page<SysOperLog> page, OperLogDTO query) {
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(SysOperLog::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getOperName())) {
            wrapper.like(SysOperLog::getOperName, query.getOperName());
        }
        if (query.getBusinessType() != null) {
            wrapper.eq(SysOperLog::getBusinessType, query.getBusinessType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysOperLog::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getOperTime())) {
            // 处理时间范围查询
            String[] params = query.getOperTime().split(",");
            if (params.length == 2) {
                wrapper.ge(SysOperLog::getOperTime, params[0]);
                wrapper.le(SysOperLog::getOperTime, params[1]);
            }
        }
        if (StringUtils.hasText(query.getParams())) {
            // 处理时间范围查询
            String[] params = query.getParams().split(",");
            if (params.length == 2) {
                wrapper.ge(SysOperLog::getOperTime, params[0]);
                wrapper.le(SysOperLog::getOperTime, params[1]);
            }
        }

        wrapper.orderByDesc(SysOperLog::getOperId);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysOperLog> selectOperLogList(OperLogDTO query) {
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(SysOperLog::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getOperName())) {
            wrapper.like(SysOperLog::getOperName, query.getOperName());
        }
        if (query.getBusinessType() != null) {
            wrapper.eq(SysOperLog::getBusinessType, query.getBusinessType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysOperLog::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(SysOperLog::getOperId);
        return this.list(wrapper);
    }

    @Override
    public SysOperLog selectOperLogById(Long operId) {
        return this.getById(operId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertOperLog(OperLogDTO operLogDTO) {
        SysOperLog operLog = new SysOperLog();
        BeanUtils.copyProperties(operLogDTO, operLog);

        return this.save(operLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOperLog(Long operId) {
        return this.removeById(operId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOperLogByIds(Long[] operIds) {
        List<Long> ids = Arrays.asList(operIds);
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanOperLog() {
        // 清空所有操作日志
        return this.remove(new LambdaQueryWrapper<>());
    }
}
