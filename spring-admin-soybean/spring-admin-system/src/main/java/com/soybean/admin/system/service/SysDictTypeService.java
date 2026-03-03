package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysDictType;
import com.soybean.admin.system.dto.DictTypeDTO;

import java.util.List;

/**
 * 字典类型服务接口
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 分页查询字典类型
     */
    IPage<SysDictType> selectDictTypePage(Page<SysDictType> page, DictTypeDTO query);

    /**
     * 查询所有字典类型
     */
    List<SysDictType> selectDictTypeList(DictTypeDTO query);

    /**
     * 根据ID查询字典类型
     */
    SysDictType selectDictTypeById(Long dictId);

    /**
     * 根据字典类型查询
     */
    SysDictType selectDictTypeByType(String dictType);

    /**
     * 新增字典类型
     */
    boolean insertDictType(DictTypeDTO dictTypeDTO);

    /**
     * 修改字典类型
     */
    boolean updateDictType(DictTypeDTO dictTypeDTO);

    /**
     * 删除字典类型
     */
    boolean deleteDictType(Long dictId);

    /**
     * 校验字典类型是否唯一
     */
    boolean checkDictTypeUnique(DictTypeDTO dictTypeDTO);
}
