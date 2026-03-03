package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysDictData;
import com.soybean.admin.data.entity.SysDictType;
import com.soybean.admin.data.mapper.SysDictDataMapper;
import com.soybean.admin.data.mapper.SysDictTypeMapper;
import com.soybean.admin.system.dto.DictTypeDTO;
import com.soybean.admin.system.service.SysDictTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 字典类型服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    private final SysDictTypeMapper dictTypeMapper;
    private final SysDictDataMapper dictDataMapper;

    @Override
    public IPage<SysDictType> selectDictTypePage(Page<SysDictType> page, DictTypeDTO query) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getDictName())) {
            wrapper.like(SysDictType::getDictName, query.getDictName());
        }
        if (StringUtils.hasText(query.getDictType())) {
            wrapper.like(SysDictType::getDictType, query.getDictType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysDictType::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(SysDictType::getDictName, query.getKeyword())
                    .or().like(SysDictType::getDictType, query.getKeyword()));
        }

        wrapper.orderByDesc(SysDictType::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysDictType> selectDictTypeList(DictTypeDTO query) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getDictName())) {
            wrapper.like(SysDictType::getDictName, query.getDictName());
        }
        if (StringUtils.hasText(query.getDictType())) {
            wrapper.like(SysDictType::getDictType, query.getDictType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysDictType::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(SysDictType::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return this.getById(dictId);
    }

    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictType::getDictType, dictType);
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDictType(DictTypeDTO dictTypeDTO) {
        // 检查字典类型是否已存在
        if (!checkDictTypeUnique(dictTypeDTO)) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "新增字典'" + dictTypeDTO.getDictName() + "'失败，字典类型已存在");
        }

        SysDictType dictType = new SysDictType();
        BeanUtils.copyProperties(dictTypeDTO, dictType);

        return this.save(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDictType(DictTypeDTO dictTypeDTO) {
        SysDictType existingDictType = this.getById(dictTypeDTO.getDictId());
        if (existingDictType == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 检查字典类型是否唯一
        if (!checkDictTypeUnique(dictTypeDTO)) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "修改字典'" + dictTypeDTO.getDictName() + "'失败，字典类型已存在");
        }

        SysDictType dictType = new SysDictType();
        BeanUtils.copyProperties(dictTypeDTO, dictType);

        return this.updateById(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictType(Long dictId) {
        // 检查是否有字典数据
        LambdaQueryWrapper<SysDictData> dataWrapper = new LambdaQueryWrapper<>();
        dataWrapper.eq(SysDictData::getDictType, this.getById(dictId).getDictType());
        long count = dictDataMapper.selectCount(dataWrapper);
        if (count > 0) {
            throw new BusinessException(ResponseCode.DATA_IN_USE, "该字典类型下有数据，不能删除");
        }

        return this.removeById(dictId);
    }

    @Override
    public boolean checkDictTypeUnique(DictTypeDTO dictTypeDTO) {
        Long dictId = dictTypeDTO.getDictId() == null ? -1L : dictTypeDTO.getDictId();
        SysDictType dictType = this.selectDictTypeByType(dictTypeDTO.getDictType());
        if (dictType != null && !dictType.getDictId().equals(dictId)) {
            return false;
        }
        return true;
    }
}
