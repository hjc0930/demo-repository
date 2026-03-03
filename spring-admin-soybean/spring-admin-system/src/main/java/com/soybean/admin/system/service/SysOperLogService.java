package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysOperLog;
import com.soybean.admin.system.dto.OperLogDTO;

import java.util.List;

/**
 * 操作日志服务接口
 */
public interface SysOperLogService extends IService<SysOperLog> {

    /**
     * 分页查询操作日志
     */
    IPage<SysOperLog> selectOperLogPage(Page<SysOperLog> page, OperLogDTO query);

    /**
     * 查询操作日志列表
     */
    List<SysOperLog> selectOperLogList(OperLogDTO query);

    /**
     * 根据ID查询操作日志
     */
    SysOperLog selectOperLogById(Long operId);

    /**
     * 新增操作日志
     */
    boolean insertOperLog(OperLogDTO operLogDTO);

    /**
     * 删除操作日志
     */
    boolean deleteOperLog(Long operId);

    /**
     * 批量删除操作日志
     */
    boolean deleteOperLogByIds(Long[] operIds);

    /**
     * 清空操作日志
     */
    boolean cleanOperLog();
}
