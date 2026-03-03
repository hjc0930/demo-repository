package com.soybean.admin.oss.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.response.PageResult;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.common.util.StringUtils;
import com.soybean.admin.data.entity.SysOss;
import com.soybean.admin.data.entity.SysOssConfig;
import com.soybean.admin.data.mapper.SysOssConfigMapper;
import com.soybean.admin.data.mapper.SysOssMapper;
import com.soybean.admin.oss.dto.FileInfo;
import com.soybean.admin.oss.service.FileStorageService;
import com.soybean.admin.tenant.context.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/system/oss")
@RequiredArgsConstructor
@Validated
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final SysOssMapper sysOssMapper;
    private final SysOssConfigMapper sysOssConfigMapper;

    /**
     * 上传文件
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<FileInfo> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("文件不能为空");
        }

        // 检查文件大小（100MB）
        long maxSize = 100 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            return Result.fail("文件大小超过限制");
        }

        FileInfo fileInfo = fileStorageService.upload(file);

        // 保存到数据库
        SysOss sysOss = new SysOss();
        sysOss.setFileName(fileInfo.getFileName());
        sysOss.setOriginalName(fileInfo.getOriginalName());
        sysOss.setFileSuffix(fileInfo.getFileSuffix());
        sysOss.setFileSize(fileInfo.getFileSize());
        sysOss.setFileType(fileInfo.getFileType());
        sysOss.setConfigId(fileInfo.getConfigId());
        sysOss.setProvider(fileInfo.getProvider());
        sysOss.setFilePath(fileInfo.getFilePath());
        sysOss.setUrl(fileInfo.getUrl());
        sysOss.setFileMd5(fileInfo.getFileMd5());
        sysOss.setTenantId(TenantContext.getTenantId());

        sysOssMapper.insert(sysOss);
        fileInfo.setId(sysOss.getId());

        return Result.success(fileInfo);
    }

    /**
     * 初始化分片上传
     */
    @PostMapping("/upload/chunk/init")
    public Result<String> initChunkUpload(
        @RequestParam String fileName,
        @RequestParam Long fileSize,
        @RequestParam String fileMd5,
        @RequestParam Integer chunkSize,
        @RequestParam Integer totalChunks
    ) {
        String uploadId = fileStorageService.initChunkUpload(fileName, fileSize, fileMd5, chunkSize, totalChunks);
        return Result.success(uploadId);
    }

    /**
     * 上传分片
     */
    @PostMapping("/upload/chunk")
    public Result<Boolean> uploadChunk(
        @RequestParam String uploadId,
        @RequestParam Integer chunkNumber,
        @RequestParam("file") MultipartFile file
    ) {
        boolean result = fileStorageService.uploadChunk(uploadId, chunkNumber, file);
        return Result.success(result);
    }

    /**
     * 完成分片上传（合并）
     */
    @PostMapping("/upload/chunk/complete")
    public Result<FileInfo> completeChunkUpload(@RequestParam String uploadId) {
        FileInfo fileInfo = fileStorageService.completeChunkUpload(uploadId);

        // 保存到数据库
        SysOss sysOss = new SysOss();
        sysOss.setFileName(fileInfo.getFileName());
        sysOss.setOriginalName(fileInfo.getOriginalName());
        sysOss.setFileSuffix(fileInfo.getFileSuffix());
        sysOss.setFileSize(fileInfo.getFileSize());
        sysOss.setConfigId(fileInfo.getConfigId());
        sysOss.setProvider(fileInfo.getProvider());
        sysOss.setFilePath(fileInfo.getFilePath());
        sysOss.setUrl(fileInfo.getUrl());
        sysOss.setFileMd5(fileInfo.getFileMd5());
        sysOss.setTenantId(TenantContext.getTenantId());

        sysOssMapper.insert(sysOss);
        fileInfo.setId(sysOss.getId());

        return Result.success(fileInfo);
    }

    /**
     * 取消分片上传
     */
    @PostMapping("/upload/chunk/abort")
    public Result<Void> abortChunkUpload(@RequestParam String uploadId) {
        fileStorageService.abortChunkUpload(uploadId);
        return Result.success();
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response) throws IOException {
        SysOss sysOss = sysOssMapper.selectById(id);
        if (sysOss == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType(sysOss.getFileType());
        response.setHeader("Content-Disposition", "attachment; filename=" +
            URLEncoder.encode(sysOss.getOriginalName(), "UTF-8"));

        try (var inputStream = fileStorageService.download(sysOss.getFilePath());
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        SysOss sysOss = sysOssMapper.selectById(id);
        if (sysOss == null) {
            return Result.fail("文件不存在");
        }

        // 删除存储文件
        fileStorageService.delete(sysOss.getFilePath());

        // 删除数据库记录
        sysOssMapper.deleteById(id);

        return Result.success();
    }

    /**
     * 批量删除文件
     */
    @DeleteMapping("/batch")
    public Result<Integer> deleteBatch(@RequestBody @NotEmpty List<Long> ids) {
        List<SysOss> sysOssList = sysOssMapper.selectBatchIds(ids);
        int count = 0;

        for (SysOss sysOss : sysOssList) {
            if (fileStorageService.delete(sysOss.getFilePath())) {
                count++;
            }
        }

        sysOssMapper.deleteBatchIds(ids);

        return Result.success(count);
    }

    /**
     * 获取文件列表
     */
    @GetMapping("/list")
    public Result<PageResult<SysOss>> list(
        @RequestParam(required(false) String fileName,
        @RequestParam(required(false) String originalName,
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Page<SysOss> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysOss> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(fileName)) {
            wrapper.like(SysOss::getFileName, fileName);
        }
        if (StringUtils.hasText(originalName)) {
            wrapper.like(SysOss::getOriginalName, originalName);
        }

        wrapper.orderByDesc(SysOss::getCreateTime);

        IPage<SysOss> result = sysOssMapper.selectPage(page, wrapper);
        return Result.success(PageResult.of(result.getRecords(), result.getTotal()));
    }

    /**
     * 获取文件详情
     */
    @GetMapping("/{id}")
    public Result<SysOss> getInfo(@PathVariable Long id) {
        SysOss sysOss = sysOssMapper.selectById(id);
        if (sysOss == null) {
            return Result.fail("文件不存在");
        }
        return Result.success(sysOss);
    }

    /**
     * 获取OSS配置列表
     */
    @GetMapping("/config/list")
    public Result<List<SysOssConfig>> listConfig() {
        List<SysOssConfig> list = sysOssConfigMapper.selectList(
            new LambdaQueryWrapper<SysOssConfig>()
                .orderByDesc(SysOssConfig::getCreateTime)
        );
        return Result.success(list);
    }

    /**
     * 获取OSS配置详情
     */
    @GetMapping("/config/{id}")
    public Result<SysOssConfig> getConfigInfo(@PathVariable Long id) {
        SysOssConfig config = sysOssConfigMapper.selectById(id);
        if (config == null) {
            return Result.fail("配置不存在");
        }
        // 隐藏敏感信息
        config.setAccessKey(maskSensitive(config.getAccessKey()));
        config.setSecretKey(maskSensitive(config.getSecretKey()));
        return Result.success(config);
    }

    /**
     * 新增OSS配置
     */
    @PostMapping("/config")
    public Result<Void> addConfig(@RequestBody @Validated SysOssConfig config) {
        // 检查是否只有一个默认配置
        if ("1".equals(config.getIsDefault())) {
            sysOssConfigMapper.update(null,
                new LambdaQueryWrapper<SysOssConfig>()
                    .eq(SysOssConfig::getProvider, config.getProvider())
                    .eq(SysOssConfig::getIsDefault, "1")
                    .set(SysOssConfig::getIsDefault, "0")
            );
        }

        sysOssConfigMapper.insert(config);
        return Result.success();
    }

    /**
     * 修改OSS配置
     */
    @PutMapping("/config")
    public Result<Void> updateConfig(@RequestBody @Validated SysOssConfig config) {
        if (config.getId() == null) {
            return Result.fail("配置ID不能为空");
        }

        // 检查是否只有一个默认配置
        if ("1".equals(config.getIsDefault())) {
            sysOssConfigMapper.update(null,
                new LambdaQueryWrapper<SysOssConfig>()
                    .eq(SysOssConfig::getProvider, config.getProvider())
                    .eq(SysOssConfig::getIsDefault, "1")
                    .ne(SysOssConfig::getId, config.getId())
                    .set(SysOssConfig::getIsDefault, "0")
            );
        }

        sysOssConfigMapper.updateById(config);
        return Result.success();
    }

    /**
     * 删除OSS配置
     */
    @DeleteMapping("/config/{id}")
    public Result<Void> deleteConfig(@PathVariable Long id) {
        sysOssConfigMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 设置默认配置
     */
    @PutMapping("/config/{id}/default")
    public Result<Void> setDefault(@PathVariable Long id) {
        SysOssConfig config = sysOssConfigMapper.selectById(id);
        if (config == null) {
            return Result.fail("配置不存在");
        }

        // 取消同类型的其他默认配置
        sysOssConfigMapper.update(null,
            new LambdaQueryWrapper<SysOssConfig>()
                .eq(SysOssConfig::getProvider, config.getProvider())
                .eq(SysOssConfig::getIsDefault, "1")
                .set(SysOssConfig::getIsDefault, "0")
        );

        // 设置当前为默认
        config.setIsDefault("1");
        sysOssConfigMapper.updateById(config);

        return Result.success();
    }

    /**
     * 测试连接
     */
    @PostMapping("/config/{id}/test")
    public Result<Boolean> testConnection(@PathVariable Long id) {
        SysOssConfig config = sysOssConfigMapper.selectById(id);
        if (config == null) {
            return Result.fail("配置不存在");
        }

        // TODO: 实现各种存储类型的连接测试
        return Result.success(true);
    }

    private String maskSensitive(String value) {
        if (value == null || value.length() <= 8) {
            return "******";
        }
        return value.substring(0, 4) + "******" + value.substring(value.length() - 4);
    }
}
