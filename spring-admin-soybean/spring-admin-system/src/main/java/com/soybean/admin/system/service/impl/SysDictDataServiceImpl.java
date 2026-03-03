package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.data.entity.SysDictData;
import com.soybean.admin.data.mapper.SysDictDataMapper;
import com.soybean.admin.system.dto.DictDataDTO;
import com.soybean.admin.system.service.SysDictDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 字典数据服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    private final SysDictDataMapper dictDataMapper;

    @Override
    public IPage<SysDictData> selectDictDataPage(Page<SysDictData> page, DictDataDTO query) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getDictType())) {
            wrapper.eq(SysDictData::getDictType, query.getDictType());
        }
        if (StringUtils.hasText(query.getDictLabel())) {
            wrapper.like(SysDictData::getDictLabel, query.getDictLabel());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysDictData::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(SysDictData::getDictLabel, query.getKeyword())
                    .or().like(SysDictData::getDictValue, query.getKeyword()));
        }

        wrapper.orderByAsc(SysDictData::getDictSort);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysDictData> selectDictDataList(DictDataDTO query) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getDictType())) {
            wrapper.eq(SysDictData::getDictType, query.getDictType());
        }
        if (StringUtils.hasText(query.getDictLabel())) {
            wrapper.like(SysDictData::getDictLabel, query.getDictLabel());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysDictData::getStatus, query.getStatus());
        }

        wrapper.orderByAsc(SysDictData::getDictSort);
        return this.list(wrapper);
    }

    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return this.getById(dictCode);
    }

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictData::getDictType, dictType);
        wrapper.eq(SysDictData::getStatus, "0");
        wrapper.orderByAsc(SysDictData::getDictSort);
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDictData(DictDataDTO dictDataDTO) {
        SysDictData dictData = new SysDictData();
        BeanUtils.copyProperties(dictDataDTO, dictData);

        return this.save(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictData(DictDataDTO dictDataDTO) {
        SysDictData dictData = new SysDictData();
        BeanUtils.copyProperties(dictDataDTO, dictData);

        return this.updateById(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictData(Long dictCode) {
        return this.removeById(dictCode);
    }
}
