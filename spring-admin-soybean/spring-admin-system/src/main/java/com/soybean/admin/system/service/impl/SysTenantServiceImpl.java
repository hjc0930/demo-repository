package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysTenant;
import com.soybean.admin.data.mapper.SysTenantMapper;
import com.soybean.admin.system.dto.TenantDTO;
import com.soybean.admin.system.service.SysTenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 租户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

    private final SysTenantMapper tenantMapper;

    @Override
    public IPage<SysTenant> selectTenantPage(Page<SysTenant> page, TenantDTO query) {
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getTenantId())) {
            wrapper.like(SysTenant::getTenantId, query.getTenantId());
        }
        if (StringUtils.hasText(query.getCompanyName())) {
            wrapper.like(SysTenant::getCompanyName, query.getCompanyName());
        }
        if (StringUtils.hasText(query.getContactUserName())) {
            wrapper.like(SysTenant::getContactUserName, query.getContactUserName());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysTenant::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(SysTenant::getCompanyName, query.getKeyword())
                    .or().like(SysTenant::getContactUserName, query.getKeyword())
                    .or().like(SysTenant::getTenantId, query.getKeyword()));
        }

        // 查询未删除的数据
        wrapper.eq(SysTenant::getDelFlag, "0");
        wrapper.orderByDesc(SysTenant::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysTenant> selectTenantList(TenantDTO query) {
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getTenantId())) {
            wrapper.like(SysTenant::getTenantId, query.getTenantId());
        }
        if (StringUtils.hasText(query.getCompanyName())) {
            wrapper.like(SysTenant::getCompanyName, query.getCompanyName());
        }
        if (StringUtils.hasText(query.getContactUserName())) {
            wrapper.like(SysTenant::getContactUserName, query.getContactUserName());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysTenant::getStatus, query.getStatus());
        }

        wrapper.eq(SysTenant::getDelFlag, "0");
        wrapper.orderByDesc(SysTenant::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysTenant selectTenantById(Long id) {
        return this.getById(id);
    }

    @Override
    public SysTenant selectTenantByTenantId(String tenantId) {
        return tenantMapper.selectByTenantId(tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertTenant(TenantDTO tenantDTO) {
        // 检查租户ID是否已存在
        if (!checkTenantIdUnique(tenantDTO.getTenantId())) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "新增租户失败，租户ID已存在");
        }

        SysTenant tenant = new SysTenant();
        BeanUtils.copyProperties(tenantDTO, tenant);

        return this.save(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTenant(TenantDTO tenantDTO) {
        SysTenant existingTenant = this.getById(tenantDTO.getId());
        if (existingTenant == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        SysTenant tenant = new SysTenant();
        BeanUtils.copyProperties(tenantDTO, tenant);

        return this.updateById(tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTenant(Long id) {
        // 逻辑删除
        SysTenant tenant = new SysTenant();
        tenant.setId(id);
        tenant.setDelFlag("2");

        return this.updateById(tenant);
    }

    @Override
    public boolean checkTenantIdUnique(String tenantId) {
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTenant::getTenantId, tenantId);
        return this.count(wrapper) == 0;
    }

    @Override
    public boolean checkTenantExists(String tenantId) {
        return tenantMapper.checkTenantExists(tenantId) > 0;
    }
}
