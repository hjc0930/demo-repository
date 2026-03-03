package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysConfig;
import com.soybean.admin.system.dto.ConfigDTO;

import java.util.List;

/**
 * 系统配置服务接口
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 分页查询系统配置
     */
    IPage<SysConfig> selectConfigPage(Page<SysConfig> page, ConfigDTO query);

    /**
     * 查询系统配置列表
     */
    List<SysConfig> selectConfigList(ConfigDTO query);

    /**
     * 根据ID查询系统配置
     */
    SysConfig selectConfigById(Long configId);

    /**
     * 根据配置键名查询配置值
     */
    String selectConfigValueByKey(String configKey);

    /**
     * 新增系统配置
     */
    boolean insertConfig(ConfigDTO configDTO);

    /**
     * 修改系统配置
     */
    boolean updateConfig(ConfigDTO configDTO);

    /**
     * 删除系统配置
     */
    boolean deleteConfig(Long configId);

    /**
     * 校验配置键名是否唯一
     */
    boolean checkConfigKeyUnique(ConfigDTO configDTO);
}
