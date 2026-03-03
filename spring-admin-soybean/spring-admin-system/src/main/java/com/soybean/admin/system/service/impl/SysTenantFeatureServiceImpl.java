package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysTenantFeature;
import com.soybean.admin.data.mapper.SysTenantFeatureMapper;
import com.soybean.admin.system.dto.TenantFeatureDTO;
import com.soybean.admin.system.service.SysTenantFeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 租户功能开关服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysTenantFeatureServiceImpl extends ServiceImpl<SysTenantFeatureMapper, SysTenantFeature> implements SysTenantFeatureService {

    private final SysTenantFeatureMapper tenantFeatureMapper;

    @Override
    public IPage<SysTenantFeature> selectTenantFeaturePage(Page<SysTenantFeature> page, TenantFeatureDTO query) {
        LambdaQueryWrapper<SysTenantFeature> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getTenantId())) {
            wrapper.eq(SysTenantFeature::getTenantId, query.getTenantId());
        }
        if (StringUtils.hasText(query.getFeatureKey())) {
            wrapper.like(SysTenantFeature::getFeatureKey, query.getFeatureKey());
        }

        wrapper.orderByDesc(SysTenantFeature::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysTenantFeature> selectTenantFeatureList(TenantFeatureDTO query) {
        LambdaQueryWrapper<SysTenantFeature> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getTenantId())) {
            wrapper.eq(SysTenantFeature::getTenantId, query.getTenantId());
        }
        if (StringUtils.hasText(query.getFeatureKey())) {
            wrapper.like(SysTenantFeature::getFeatureKey, query.getFeatureKey());
        }

        wrapper.orderByDesc(SysTenantFeature::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysTenantFeature selectTenantFeatureById(Long id) {
        return this.getById(id);
    }

    @Override
    public SysTenantFeature selectTenantFeature(String tenantId, String featureKey) {
        LambdaQueryWrapper<SysTenantFeature> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTenantFeature::getTenantId, tenantId);
        wrapper.eq(SysTenantFeature::getFeatureKey, featureKey);
        return this.getOne(wrapper);
    }

    @Override
    public boolean isFeatureEnabled(String tenantId, String featureKey) {
        SysTenantFeature feature = selectTenantFeature(tenantId, featureKey);
        return feature != null && feature.getEnabled();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertTenantFeature(TenantFeatureDTO tenantFeatureDTO) {
        // 检查功能是否已存在
        SysTenantFeature existing = selectTenantFeature(tenantFeatureDTO.getTenantId(), tenantFeatureDTO.getFeatureKey());
        if (existing != null) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "功能开关已存在");
        }

        SysTenantFeature tenantFeature = new SysTenantFeature();
        BeanUtils.copyProperties(tenantFeatureDTO, tenantFeature);

        return this.save(tenantFeature);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTenantFeature(TenantFeatureDTO tenantFeatureDTO) {
        SysTenantFeature existing = this.getById(tenantFeatureDTO.getId());
        if (existing == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        SysTenantFeature tenantFeature = new SysTenantFeature();
        BeanUtils.copyProperties(tenantFeatureDTO, tenantFeature);

        return this.updateById(tenantFeature);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTenantFeature(Long id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleFeature(String tenantId, String featureKey, boolean enabled) {
        SysTenantFeature feature = selectTenantFeature(tenantId, featureKey);
        if (feature == null) {
            // 创建新的功能开关
            feature = new SysTenantFeature();
            feature.setTenantId(tenantId);
            feature.setFeatureKey(featureKey);
            feature.setEnabled(enabled);
            return this.save(feature);
        } else {
            // 更新现有功能开关
            feature.setEnabled(enabled);
            return this.updateById(feature);
        }
    }
}
