package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysTenantPackage;
import com.soybean.admin.data.mapper.SysTenantPackageMapper;
import com.soybean.admin.system.dto.TenantPackageDTO;
import com.soybean.admin.system.service.SysTenantPackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 租户套餐服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysTenantPackageServiceImpl extends ServiceImpl<SysTenantPackageMapper, SysTenantPackage> implements SysTenantPackageService {

    private final SysTenantPackageMapper tenantPackageMapper;

    @Override
    public IPage<SysTenantPackage> selectTenantPackagePage(Page<SysTenantPackage> page, TenantPackageDTO query) {
        LambdaQueryWrapper<SysTenantPackage> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getPackageName())) {
            wrapper.like(SysTenantPackage::getPackageName, query.getPackageName());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysTenantPackage::getStatus, query.getStatus());
        }

        wrapper.eq(SysTenantPackage::getDelFlag, "0");
        wrapper.orderByDesc(SysTenantPackage::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysTenantPackage> selectTenantPackageList(TenantPackageDTO query) {
        LambdaQueryWrapper<SysTenantPackage> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getPackageName())) {
            wrapper.like(SysTenantPackage::getPackageName, query.getPackageName());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysTenantPackage::getStatus, query.getStatus());
        }

        wrapper.eq(SysTenantPackage::getDelFlag, "0");
        wrapper.orderByDesc(SysTenantPackage::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysTenantPackage selectTenantPackageById(Integer packageId) {
        return this.getById(packageId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertTenantPackage(TenantPackageDTO tenantPackageDTO) {
        SysTenantPackage tenantPackage = new SysTenantPackage();
        BeanUtils.copyProperties(tenantPackageDTO, tenantPackage);
        tenantPackage.setDelFlag("0");

        return this.save(tenantPackage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTenantPackage(TenantPackageDTO tenantPackageDTO) {
        SysTenantPackage existing = this.getById(tenantPackageDTO.getPackageId());
        if (existing == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        SysTenantPackage tenantPackage = new SysTenantPackage();
        BeanUtils.copyProperties(tenantPackageDTO, tenantPackage);

        return this.updateById(tenantPackage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTenantPackage(Integer packageId) {
        SysTenantPackage tenantPackage = new SysTenantPackage();
        tenantPackage.setPackageId(packageId);
        tenantPackage.setDelFlag("2");

        return this.updateById(tenantPackage);
    }
}
