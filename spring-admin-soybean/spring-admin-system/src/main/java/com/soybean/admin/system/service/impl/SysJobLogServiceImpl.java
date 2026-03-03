package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysJobLog;
import com.soybean.admin.data.mapper.SysJobLogMapper;
import com.soybean.admin.system.dto.JobLogDTO;
import com.soybean.admin.system.service.SysJobLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 定时任务日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements SysJobLogService {

    private final SysJobLogMapper jobLogMapper;

    @Override
    public IPage<SysJobLog> selectJobLogPage(Page<SysJobLog> page, JobLogDTO query) {
        LambdaQueryWrapper<SysJobLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getJobName())) {
            wrapper.like(SysJobLog::getJobName, query.getJobName());
        }
        if (StringUtils.hasText(query.getJobGroup())) {
            wrapper.eq(SysJobLog::getJobGroup, query.getJobGroup());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysJobLog::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getParams())) {
            // 处理时间范围查询
            String[] params = query.getParams().split(",");
            if (params.length == 2) {
                wrapper.ge(SysJobLog::getCreateTime, params[0]);
                wrapper.le(SysJobLog::getCreateTime, params[1]);
            }
        }

        wrapper.orderByDesc(SysJobLog::getJobLogId);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysJobLog> selectJobLogList(JobLogDTO query) {
        LambdaQueryWrapper<SysJobLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getJobName())) {
            wrapper.like(SysJobLog::getJobName, query.getJobName());
        }
        if (StringUtils.hasText(query.getJobGroup())) {
            wrapper.eq(SysJobLog::getJobGroup, query.getJobGroup());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysJobLog::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(SysJobLog::getJobLogId);
        return this.list(wrapper);
    }

    @Override
    public SysJobLog selectJobLogById(Long jobLogId) {
        return this.getById(jobLogId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertJobLog(JobLogDTO jobLogDTO) {
        SysJobLog jobLog = new SysJobLog();
        BeanUtils.copyProperties(jobLogDTO, jobLog);

        return this.save(jobLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteJobLog(Long jobLogId) {
        return this.removeById(jobLogId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteJobLogByIds(Long[] jobLogIds) {
        List<Long> ids = Arrays.asList(jobLogIds);
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanJobLog() {
        // 清空所有任务日志
        return this.remove(new LambdaQueryWrapper<>());
    }
}
