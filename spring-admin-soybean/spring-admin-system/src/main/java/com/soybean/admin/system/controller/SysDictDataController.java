package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysDictData;
import com.soybean.admin.system.dto.DictDataDTO;
import com.soybean.admin.system.service.SysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典数据控制器
 */
@RestController
@RequestMapping("/api/system/dict/data")
@RequiredArgsConstructor
public class SysDictDataController {

    private final SysDictDataService dictDataService;

    /**
     * 分页查询字典数据
     */
    @GetMapping("/page")
    @RequirePermission("system:dict:list")
    public Result<IPage<SysDictData>> page(Page<SysDictData> page, DictDataDTO query) {
        IPage<SysDictData> result = dictDataService.selectDictDataPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询字典数据列表
     */
    @GetMapping("/list")
    @RequirePermission("system:dict:list")
    public Result<List<SysDictData>> list(DictDataDTO query) {
        List<SysDictData> list = dictDataService.selectDictDataList(query);
        return Result.ok(list);
    }

    /**
     * 根据字典类型查询字典数据
     */
    @GetMapping("/type/{dictType}")
    @RequirePermission("system:dict:query")
    public Result<List<SysDictData>> getDictDataByType(@PathVariable String dictType) {
        List<SysDictData> list = dictDataService.selectDictDataByType(dictType);
        return Result.ok(list);
    }

    /**
     * 根据ID查询字典数据
     */
    @GetMapping("/{dictCode}")
    @RequirePermission("system:dict:query")
    public Result<SysDictData> getDictData(@PathVariable Long dictCode) {
        SysDictData dictData = dictDataService.selectDictDataById(dictCode);
        return Result.ok(dictData);
    }

    /**
     * 新增字典数据
     */
    @PostMapping
    @RequirePermission("system:dict:add")
    @Log(title = "字典数据", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody DictDataDTO dictDataDTO) {
        dictDataService.insertDictData(dictDataDTO);
        return Result.ok();
    }

    /**
     * 修改字典数据
     */
    @PutMapping
    @RequirePermission("system:dict:edit")
    @Log(title = "字典数据", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody DictDataDTO dictDataDTO) {
        dictDataService.updateDictData(dictDataDTO);
        return Result.ok();
    }

    /**
     * 删除字典数据
     */
    @DeleteMapping("/{dictCode}")
    @RequirePermission("system:dict:remove")
    @Log(title = "字典数据", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long dictCode) {
        dictDataService.deleteDictData(dictCode);
        return Result.ok();
    }
}
