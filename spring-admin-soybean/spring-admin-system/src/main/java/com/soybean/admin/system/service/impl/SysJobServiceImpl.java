package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysJob;
import com.soybean.admin.data.mapper.SysJobMapper;
import com.soybean.admin.system.dto.JobDTO;
import com.soybean.admin.system.quartz.QuartzManage;
import com.soybean.admin.system.service.SysJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 定时任务服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements SysJobService {

    private final SysJobMapper jobMapper;
    private final QuartzManage quartzManage;

    @Override
    public IPage<SysJob> selectJobPage(Page<SysJob> page, JobDTO query) {
        LambdaQueryWrapper<SysJob> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getJobName())) {
            wrapper.like(SysJob::getJobName, query.getJobName());
        }
        if (StringUtils.hasText(query.getJobGroup())) {
            wrapper.eq(SysJob::getJobGroup, query.getJobGroup());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysJob::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(SysJob::getJobName, query.getKeyword())
                    .or().like(SysJob::getInvokeTarget, query.getKeyword()));
        }

        wrapper.orderByDesc(SysJob::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysJob> selectJobList(JobDTO query) {
        LambdaQueryWrapper<SysJob> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getJobName())) {
            wrapper.like(SysJob::getJobName, query.getJobName());
        }
        if (StringUtils.hasText(query.getJobGroup())) {
            wrapper.eq(SysJob::getJobGroup, query.getJobGroup());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysJob::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(SysJob::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysJob selectJobById(Long jobId) {
        return this.getById(jobId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertJob(JobDTO jobDTO) {
        SysJob job = new SysJob();
        BeanUtils.copyProperties(jobDTO, job);

        // 保存到数据库
        boolean result = this.save(job);

        if (result) {
            try {
                // 创建Quartz任务
                quartzManage.createJob(job);
            } catch (Exception e) {
                log.error("Failed to create Quartz job: {}", job.getJobName(), e);
                throw new BusinessException("创建定时任务失败: " + e.getMessage());
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateJob(JobDTO jobDTO) {
        SysJob existingJob = this.getById(jobDTO.getJobId());
        if (existingJob == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        SysJob job = new SysJob();
        BeanUtils.copyProperties(jobDTO, job);

        // 更新数据库
        boolean result = this.updateById(job);

        if (result) {
            try {
                // 更新Quartz任务
                quartzManage.createJob(job);
            } catch (Exception e) {
                log.error("Failed to update Quartz job: {}", job.getJobName(), e);
                throw new BusinessException("更新定时任务失败: " + e.getMessage());
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteJob(Long jobId) {
        SysJob job = this.getById(jobId);
        if (job == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 删除Quartz任务
        try {
            quartzManage.deleteJob(jobId.toString(), job.getJobGroup());
        } catch (Exception e) {
            log.error("Failed to delete Quartz job: {}", job.getJobName(), e);
        }

        // 删除数据库记录
        return this.removeById(jobId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startJob(Long jobId) {
        SysJob job = this.getById(jobId);
        if (job == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        try {
            // 恢复Quartz任务
            quartzManage.resumeJob(jobId.toString(), job.getJobGroup());

            // 更新数据库状态
            job.setStatus("0");
            return this.updateById(job);
        } catch (Exception e) {
            log.error("Failed to start job: {}", job.getJobName(), e);
            throw new BusinessException("启动任务失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stopJob(Long jobId) {
        SysJob job = this.getById(jobId);
        if (job == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        try {
            // 暂停Quartz任务
            quartzManage.pauseJob(jobId.toString(), job.getJobGroup());

            // 更新数据库状态
            job.setStatus("1");
            return this.updateById(job);
        } catch (Exception e) {
            log.error("Failed to stop job: {}", job.getJobName(), e);
            throw new BusinessException("暂停任务失败: " + e.getMessage());
        }
    }

    @Override
    public boolean executeJob(Long jobId) {
        SysJob job = this.getById(jobId);
        if (job == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        try {
            // 立即执行任务
            quartzManage.executeJob(jobId.toString(), job.getJobGroup());
            return true;
        } catch (Exception e) {
            log.error("Failed to execute job: {}", job.getJobName(), e);
            throw new BusinessException("执行任务失败: " + e.getMessage());
        }
    }
}
