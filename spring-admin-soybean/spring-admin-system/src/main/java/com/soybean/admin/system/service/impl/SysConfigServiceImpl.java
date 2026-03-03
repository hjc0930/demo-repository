package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysConfig;
import com.soybean.admin.data.mapper.SysConfigMapper;
import com.soybean.admin.system.dto.ConfigDTO;
import com.soybean.admin.system.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 系统配置服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    private final SysConfigMapper configMapper;

    @Override
    public IPage<SysConfig> selectConfigPage(Page<SysConfig> page, ConfigDTO query) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getConfigName())) {
            wrapper.like(SysConfig::getConfigName, query.getConfigName());
        }
        if (StringUtils.hasText(query.getConfigKey())) {
            wrapper.like(SysConfig::getConfigKey, query.getConfigKey());
        }
        if (StringUtils.hasText(query.getConfigType())) {
            wrapper.eq(SysConfig::getConfigType, query.getConfigType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysConfig::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(SysConfig::getConfigName, query.getKeyword())
                    .or().like(SysConfig::getConfigKey, query.getKeyword()));
        }

        wrapper.orderByDesc(SysConfig::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysConfig> selectConfigList(ConfigDTO query) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getConfigName())) {
            wrapper.like(SysConfig::getConfigName, query.getConfigName());
        }
        if (StringUtils.hasText(query.getConfigKey())) {
            wrapper.like(SysConfig::getConfigKey, query.getConfigKey());
        }
        if (StringUtils.hasText(query.getConfigType())) {
            wrapper.eq(SysConfig::getConfigType, query.getConfigType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysConfig::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(SysConfig::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysConfig selectConfigById(Long configId) {
        return this.getById(configId);
    }

    @Override
    public String selectConfigValueByKey(String configKey) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigKey, configKey);
        wrapper.eq(SysConfig::getStatus, "0");
        SysConfig config = this.getOne(wrapper);
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertConfig(ConfigDTO configDTO) {
        // 检查配置键名是否已存在
        if (!checkConfigKeyUnique(configDTO)) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "新增参数'" + configDTO.getConfigName() + "'失败，参数键名已存在");
        }

        SysConfig config = new SysConfig();
        BeanUtils.copyProperties(configDTO, config);

        return this.save(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(ConfigDTO configDTO) {
        SysConfig existingConfig = this.getById(configDTO.getConfigId());
        if (existingConfig == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 检查配置键名是否唯一
        if (!checkConfigKeyUnique(configDTO)) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "修改参数'" + configDTO.getConfigName() + "'失败，参数键名已存在");
        }

        SysConfig config = new SysConfig();
        BeanUtils.copyProperties(configDTO, config);

        return this.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfig(Long configId) {
        SysConfig config = this.getById(configId);
        if (config == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 系统内置配置不允许删除
        if ("Y".equals(config.getConfigType())) {
            throw new BusinessException(ResponseCode.DATA_CANNOT_DELETE, "系统内置配置不能删除");
        }

        return this.removeById(configId);
    }

    @Override
    public boolean checkConfigKeyUnique(ConfigDTO configDTO) {
        Long configId = configDTO.getConfigId() == null ? -1L : configDTO.getConfigId();
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysConfig::getConfigKey, configDTO.getConfigKey());
        SysConfig config = this.getOne(wrapper);
        if (config != null && !config.getConfigId().equals(configId)) {
            return false;
        }
        return true;
    }
}
