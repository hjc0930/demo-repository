package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysFileFolder;
import com.soybean.admin.system.dto.FileFolderDTO;
import com.soybean.admin.system.service.SysFileFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件夹控制器
 */
@RestController
@RequestMapping("/api/system/folder")
@RequiredArgsConstructor
public class SysFileFolderController {

    private final SysFileFolderService fileFolderService;

    /**
     * 分页查询文件夹
     */
    @GetMapping("/page")
    @RequirePermission("system:folder:list")
    public Result<IPage<SysFileFolder>> page(Page<SysFileFolder> page, FileFolderDTO query) {
        IPage<SysFileFolder> result = fileFolderService.selectFileFolderPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询文件夹列表
     */
    @GetMapping("/list")
    @RequirePermission("system:folder:list")
    public Result<List<SysFileFolder>> list(FileFolderDTO query) {
        List<SysFileFolder> list = fileFolderService.selectFileFolderList(query);
        return Result.ok(list);
    }

    /**
     * 查询文件夹树
     */
    @GetMapping("/tree")
    @RequirePermission("system:folder:list")
    public Result<List<SysFileFolder>> tree(FileFolderDTO query) {
        List<SysFileFolder> tree = fileFolderService.selectFileFolderTree(query);
        return Result.ok(tree);
    }

    /**
     * 根据ID查询文件夹
     */
    @GetMapping("/{folderId}")
    @RequirePermission("system:folder:query")
    public Result<SysFileFolder> getFileFolder(@PathVariable Long folderId) {
        SysFileFolder fileFolder = fileFolderService.selectFileFolderById(folderId);
        return Result.ok(fileFolder);
    }

    /**
     * 根据父ID查询子文件夹
     */
    @GetMapping("/children/{parentId}")
    @RequirePermission("system:folder:query")
    public Result<List<SysFileFolder>> getChildren(@PathVariable Integer parentId) {
        List<SysFileFolder> list = fileFolderService.selectFileFoldersByParentId(parentId);
        return Result.ok(list);
    }

    /**
     * 新增文件夹
     */
    @PostMapping
    @RequirePermission("system:folder:add")
    @Log(title = "文件夹管理", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody FileFolderDTO fileFolderDTO) {
        fileFolderService.insertFileFolder(fileFolderDTO);
        return Result.ok();
    }

    /**
     * 修改文件夹
     */
    @PutMapping
    @RequirePermission("system:folder:edit")
    @Log(title = "文件夹管理", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody FileFolderDTO fileFolderDTO) {
        fileFolderService.updateFileFolder(fileFolderDTO);
        return Result.ok();
    }

    /**
     * 删除文件夹
     */
    @DeleteMapping("/{folderId}")
    @RequirePermission("system:folder:remove")
    @Log(title = "文件夹管理", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long folderId) {
        fileFolderService.deleteFileFolder(folderId);
        return Result.ok();
    }
}
