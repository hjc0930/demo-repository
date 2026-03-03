package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysJob;
import com.soybean.admin.system.dto.JobDTO;

import java.util.List;

/**
 * 定时任务服务接口
 */
public interface SysJobService extends IService<SysJob> {

    /**
     * 分页查询定时任务
     */
    IPage<SysJob> selectJobPage(Page<SysJob> page, JobDTO query);

    /**
     * 查询定时任务列表
     */
    List<SysJob> selectJobList(JobDTO query);

    /**
     * 根据ID查询定时任务
     */
    SysJob selectJobById(Long jobId);

    /**
     * 新增定时任务
     */
    boolean insertJob(JobDTO jobDTO);

    /**
     * 修改定时任务
     */
    boolean updateJob(JobDTO jobDTO);

    /**
     * 删除定时任务
     */
    boolean deleteJob(Long jobId);

    /**
     * 启动任务
     */
    boolean startJob(Long jobId);

    /**
     * 暂停任务
     */
    boolean stopJob(Long jobId);

    /**
     * 执行任务
     */
    boolean executeJob(Long jobId);
}
