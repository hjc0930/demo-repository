package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysTenantQuota;
import com.soybean.admin.data.mapper.SysTenantQuotaMapper;
import com.soybean.admin.system.dto.TenantQuotaDTO;
import com.soybean.admin.system.service.SysTenantQuotaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 租户配额服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysTenantQuotaServiceImpl extends ServiceImpl<SysTenantQuotaMapper, SysTenantQuota> implements SysTenantQuotaService {

    private final SysTenantQuotaMapper tenantQuotaMapper;

    @Override
    public IPage<SysTenantQuota> selectTenantQuotaPage(Page<SysTenantQuota> page, TenantQuotaDTO query) {
        LambdaQueryWrapper<SysTenantQuota> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getTenantId())) {
            wrapper.eq(SysTenantQuota::getTenantId, query.getTenantId());
        }

        wrapper.orderByDesc(SysTenantQuota::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysTenantQuota> selectTenantQuotaList(TenantQuotaDTO query) {
        LambdaQueryWrapper<SysTenantQuota> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getTenantId())) {
            wrapper.eq(SysTenantQuota::getTenantId, query.getTenantId());
        }

        wrapper.orderByDesc(SysTenantQuota::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysTenantQuota selectTenantQuotaById(Long id) {
        return this.getById(id);
    }

    @Override
    public SysTenantQuota selectTenantQuotaByTenantId(String tenantId) {
        LambdaQueryWrapper<SysTenantQuota> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTenantQuota::getTenantId, tenantId);
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertTenantQuota(TenantQuotaDTO tenantQuotaDTO) {
        // 检查租户配额是否已存在
        SysTenantQuota existing = selectTenantQuotaByTenantId(tenantQuotaDTO.getTenantId());
        if (existing != null) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "租户配额已存在");
        }

        SysTenantQuota tenantQuota = new SysTenantQuota();
        BeanUtils.copyProperties(tenantQuotaDTO, tenantQuota);

        return this.save(tenantQuota);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTenantQuota(TenantQuotaDTO tenantQuotaDTO) {
        SysTenantQuota existing = this.getById(tenantQuotaDTO.getId());
        if (existing == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        SysTenantQuota tenantQuota = new SysTenantQuota();
        BeanUtils.copyProperties(tenantQuotaDTO, tenantQuota);

        return this.updateById(tenantQuota);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTenantQuota(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean checkTenantQuota(String tenantId, String quotaType) {
        SysTenantQuota quota = selectTenantQuotaByTenantId(tenantId);
        if (quota == null) {
            return true;
        }

        switch (quotaType) {
            case "user":
                return quota.getUserQuota() == -1 || quota.getUserUsed() < quota.getUserQuota();
            case "storage":
                return quota.getStorageQuota() == -1 || quota.getStorageUsed() < quota.getStorageQuota();
            case "api":
                return quota.getApiQuota() == -1 || quota.getApiUsed() < quota.getApiQuota();
            default:
                return true;
        }
    }
}
