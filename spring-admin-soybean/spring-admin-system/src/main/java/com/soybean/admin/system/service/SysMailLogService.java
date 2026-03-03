package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysMailLog;
import com.soybean.admin.system.dto.MailLogDTO;

import java.util.List;

/**
 * 邮件日志服务接口
 */
public interface SysMailLogService extends IService<SysMailLog> {

    /**
     * 分页查询邮件日志
     */
    IPage<SysMailLog> selectMailLogPage(Page<SysMailLog> page, MailLogDTO query);

    /**
     * 查询邮件日志列表
     */
    List<SysMailLog> selectMailLogList(MailLogDTO query);

    /**
     * 根据ID查询邮件日志
     */
    SysMailLog selectMailLogById(Long id);

    /**
     * 新增邮件日志
     */
    boolean insertMailLog(MailLogDTO mailLogDTO);

    /**
     * 批量删除邮件日志
     */
    boolean deleteMailLogByIds(Long[] ids);

    /**
     * 清空邮件日志
     */
    boolean cleanMailLog();
}
