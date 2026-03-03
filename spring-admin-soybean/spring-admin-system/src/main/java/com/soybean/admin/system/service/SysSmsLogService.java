package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysSmsLog;
import com.soybean.admin.system.dto.SmsLogDTO;

import java.util.List;

/**
 * 短信日志服务接口
 */
public interface SysSmsLogService extends IService<SysSmsLog> {

    /**
     * 分页查询短信日志
     */
    IPage<SysSmsLog> selectSmsLogPage(Page<SysSmsLog> page, SmsLogDTO query);

    /**
     * 查询短信日志列表
     */
    List<SysSmsLog> selectSmsLogList(SmsLogDTO query);

    /**
     * 根据ID查询短信日志
     */
    SysSmsLog selectSmsLogById(Long id);

    /**
     * 新增短信日志
     */
    boolean insertSmsLog(SmsLogDTO smsLogDTO);

    /**
     * 批量删除短信日志
     */
    boolean deleteSmsLogByIds(Long[] ids);

    /**
     * 清空短信日志
     */
    boolean cleanSmsLog();
}
