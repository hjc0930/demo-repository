package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysJobLog;
import com.soybean.admin.system.dto.JobLogDTO;

import java.util.List;

/**
 * 定时任务日志服务接口
 */
public interface SysJobLogService extends IService<SysJobLog> {

    /**
     * 分页查询定时任务日志
     */
    IPage<SysJobLog> selectJobLogPage(Page<SysJobLog> page, JobLogDTO query);

    /**
     * 查询定时任务日志列表
     */
    List<SysJobLog> selectJobLogList(JobLogDTO query);

    /**
     * 根据ID查询定时任务日志
     */
    SysJobLog selectJobLogById(Long jobLogId);

    /**
     * 新增定时任务日志
     */
    boolean insertJobLog(JobLogDTO jobLogDTO);

    /**
     * 删除定时任务日志
     */
    boolean deleteJobLog(Long jobLogId);

    /**
     * 批量删除定时任务日志
     */
    boolean deleteJobLogByIds(Long[] jobLogIds);

    /**
     * 清空定时任务日志
     */
    boolean cleanJobLog();
}
