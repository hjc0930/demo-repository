package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysDictData;
import com.soybean.admin.system.dto.DictDataDTO;

import java.util.List;

/**
 * 字典数据服务接口
 */
public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 分页查询字典数据
     */
    IPage<SysDictData> selectDictDataPage(Page<SysDictData> page, DictDataDTO query);

    /**
     * 查询字典数据列表
     */
    List<SysDictData> selectDictDataList(DictDataDTO query);

    /**
     * 根据ID查询字典数据
     */
    SysDictData selectDictDataById(Long dictCode);

    /**
     * 根据字典类型查询字典数据
     */
    List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 新增字典数据
     */
    boolean insertDictData(DictDataDTO dictDataDTO);

    /**
     * 修改字典数据
     */
    boolean updateDictData(DictDataDTO dictDataDTO);

    /**
     * 删除字典数据
     */
    boolean deleteDictData(Long dictCode);
}
