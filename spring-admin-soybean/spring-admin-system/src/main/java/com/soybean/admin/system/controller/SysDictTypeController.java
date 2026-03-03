package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysDictType;
import com.soybean.admin.system.dto.DictTypeDTO;
import com.soybean.admin.system.service.SysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典类型控制器
 */
@RestController
@RequestMapping("/api/system/dict/type")
@RequiredArgsConstructor
public class SysDictTypeController {

    private final SysDictTypeService dictTypeService;

    /**
     * 分页查询字典类型
     */
    @GetMapping("/page")
    @RequirePermission("system:dict:list")
    public Result<IPage<SysDictType>> page(Page<SysDictType> page, DictTypeDTO query) {
        IPage<SysDictType> result = dictTypeService.selectDictTypePage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询字典类型列表
     */
    @GetMapping("/list")
    @RequirePermission("system:dict:list")
    public Result<List<SysDictType>> list(DictTypeDTO query) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询字典类型
     */
    @GetMapping("/{dictId}")
    @RequirePermission("system:dict:query")
    public Result<SysDictType> getDictType(@PathVariable Long dictId) {
        SysDictType dictType = dictTypeService.selectDictTypeById(dictId);
        return Result.ok(dictType);
    }

    /**
     * 根据字典类型查询
     */
    @GetMapping("/type/{dictType}")
    @RequirePermission("system:dict:query")
    public Result<SysDictType> getDictTypeByType(@PathVariable String dictType) {
        SysDictType result = dictTypeService.selectDictTypeByType(dictType);
        return Result.ok(result);
    }

    /**
     * 新增字典类型
     */
    @PostMapping
    @RequirePermission("system:dict:add")
    @Log(title = "字典类型", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody DictTypeDTO dictTypeDTO) {
        dictTypeService.insertDictType(dictTypeDTO);
        return Result.ok();
    }

    /**
     * 修改字典类型
     */
    @PutMapping
    @RequirePermission("system:dict:edit")
    @Log(title = "字典类型", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody DictTypeDTO dictTypeDTO) {
        dictTypeService.updateDictType(dictTypeDTO);
        return Result.ok();
    }

    /**
     * 删除字典类型
     */
    @DeleteMapping("/{dictId}")
    @RequirePermission("system:dict:remove")
    @Log(title = "字典类型", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long dictId) {
        dictTypeService.deleteDictType(dictId);
        return Result.ok();
    }

    /**
     * 校验字典类型是否唯一
     */
    @GetMapping("/check/{dictType}")
    @RequirePermission("system:dict:query")
    public Result<Boolean> checkDictTypeUnique(@PathVariable String dictType) {
        DictTypeDTO dictTypeDTO = new DictTypeDTO();
        dictTypeDTO.setDictType(dictType);
        boolean result = dictTypeService.checkDictTypeUnique(dictTypeDTO);
        return Result.ok(result);
    }
}
