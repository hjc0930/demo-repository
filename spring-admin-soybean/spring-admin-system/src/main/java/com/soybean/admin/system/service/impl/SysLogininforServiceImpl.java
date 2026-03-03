package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysLogininfor;
import com.soybean.admin.data.mapper.SysLogininforMapper;
import com.soybean.admin.system.dto.LogininforDTO;
import com.soybean.admin.system.service.SysLogininforService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 登录日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLogininforServiceImpl extends ServiceImpl<SysLogininforMapper, SysLogininfor> implements SysLogininforService {

    private final SysLogininforMapper logininforMapper;

    @Override
    public IPage<SysLogininfor> selectLogininforPage(Page<SysLogininfor> page, LogininforDTO query) {
        LambdaQueryWrapper<SysLogininfor> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getUserName())) {
            wrapper.like(SysLogininfor::getUserName, query.getUserName());
        }
        if (StringUtils.hasText(query.getIpaddr())) {
            wrapper.eq(SysLogininfor::getIpaddr, query.getIpaddr());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysLogininfor::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getParams())) {
            // 处理时间范围查询
            String[] params = query.getParams().split(",");
            if (params.length == 2) {
                wrapper.ge(SysLogininfor::getLoginTime, params[0]);
                wrapper.le(SysLogininfor::getLoginTime, params[1]);
            }
        }

        // 查询未删除的数据
        wrapper.eq(SysLogininfor::getDelFlag, "0");
        wrapper.orderByDesc(SysLogininfor::getInfoId);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysLogininfor> selectLogininforList(LogininforDTO query) {
        LambdaQueryWrapper<SysLogininfor> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getUserName())) {
            wrapper.like(SysLogininfor::getUserName, query.getUserName());
        }
        if (StringUtils.hasText(query.getIpaddr())) {
            wrapper.eq(SysLogininfor::getIpaddr, query.getIpaddr());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysLogininfor::getStatus, query.getStatus());
        }

        wrapper.eq(SysLogininfor::getDelFlag, "0");
        wrapper.orderByDesc(SysLogininfor::getInfoId);
        return this.list(wrapper);
    }

    @Override
    public SysLogininfor selectLogininforById(Long infoId) {
        return this.getById(infoId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertLogininfor(LogininforDTO logininforDTO) {
        SysLogininfor logininfor = new SysLogininfor();
        BeanUtils.copyProperties(logininforDTO, logininfor);
        logininfor.setDelFlag("0");

        return this.save(logininfor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLogininfor(Long infoId) {
        // 逻辑删除
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setInfoId(infoId);
        logininfor.setDelFlag("2");

        return this.updateById(logininfor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLogininforByIds(Long[] infoIds) {
        // 批量逻辑删除
        List<Long> ids = Arrays.asList(infoIds);
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanLogininfor() {
        // 清空所有登录日志
        LambdaQueryWrapper<SysLogininfor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysLogininfor::getDelFlag, "0");
        return this.remove(wrapper);
    }
}
