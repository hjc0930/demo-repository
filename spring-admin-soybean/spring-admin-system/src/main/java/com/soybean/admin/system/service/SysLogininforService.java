package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysLogininfor;
import com.soybean.admin.system.dto.LogininforDTO;

import java.util.List;

/**
 * 登录日志服务接口
 */
public interface SysLogininforService extends IService<SysLogininfor> {

    /**
     * 分页查询登录日志
     */
    IPage<SysLogininfor> selectLogininforPage(Page<SysLogininfor> page, LogininforDTO query);

    /**
     * 查询登录日志列表
     */
    List<SysLogininfor> selectLogininforList(LogininforDTO query);

    /**
     * 根据ID查询登录日志
     */
    SysLogininfor selectLogininforById(Long infoId);

    /**
     * 新增登录日志
     */
    boolean insertLogininfor(LogininforDTO logininforDTO);

    /**
     * 删除登录日志
     */
    boolean deleteLogininfor(Long infoId);

    /**
     * 批量删除登录日志
     */
    boolean deleteLogininforByIds(Long[] infoIds);

    /**
     * 清空登录日志
     */
    boolean cleanLogininfor();
}
