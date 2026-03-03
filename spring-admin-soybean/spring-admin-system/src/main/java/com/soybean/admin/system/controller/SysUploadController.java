package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysUpload;
import com.soybean.admin.system.dto.UploadDTO;
import com.soybean.admin.system.service.SysUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/system/upload")
@RequiredArgsConstructor
public class SysUploadController {

    private final SysUploadService uploadService;

    /**
     * 分页查询文件
     */
    @GetMapping("/page")
    @RequirePermission("system:upload:list")
    public Result<IPage<SysUpload>> page(Page<SysUpload> page, UploadDTO query) {
        IPage<SysUpload> result = uploadService.selectUploadPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询文件列表
     */
    @GetMapping("/list")
    @RequirePermission("system:upload:list")
    public Result<List<SysUpload>> list(UploadDTO query) {
        List<SysUpload> list = uploadService.selectUploadList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询文件
     */
    @GetMapping("/{uploadId}")
    @RequirePermission("system:upload:query")
    public Result<SysUpload> getUpload(@PathVariable String uploadId) {
        SysUpload upload = uploadService.selectUploadById(uploadId);
        return Result.ok(upload);
    }

    /**
     * 根据文件夹ID查询文件
     */
    @GetMapping("/folder/{folderId}")
    @RequirePermission("system:upload:query")
    public Result<List<SysUpload>> getUploadsByFolderId(@PathVariable Integer folderId) {
        List<SysUpload> list = uploadService.selectUploadsByFolderId(folderId);
        return Result.ok(list);
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Log(title = "文件上传", businessType = Log.BusinessType.INSERT)
    public Result<SysUpload> upload(@RequestParam("file") MultipartFile file,
                                     @RequestParam(value = "folderId", required = false) Integer folderId) {
        // TODO: 实现文件上传逻辑
        // 1. 保存文件到本地或OSS
        // 2. 生成文件记录
        // 3. 返回文件信息
        return Result.ok();
    }

    /**
     * 修改文件记录
     */
    @PutMapping
    @RequirePermission("system:upload:edit")
    @Log(title = "文件管理", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody UploadDTO uploadDTO) {
        uploadService.updateUpload(uploadDTO);
        return Result.ok();
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{uploadId}")
    @RequirePermission("system:upload:remove")
    @Log(title = "文件管理", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable String uploadId) {
        uploadService.deleteUpload(uploadId);
        return Result.ok();
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{uploadId}")
    @RequirePermission("system:upload:query")
    public Result<String> download(@PathVariable String uploadId) {
        // TODO: 实现文件下载逻辑
        SysUpload upload = uploadService.selectUploadById(uploadId);
        return Result.ok(upload != null ? upload.getUrl() : null);
    }
}
