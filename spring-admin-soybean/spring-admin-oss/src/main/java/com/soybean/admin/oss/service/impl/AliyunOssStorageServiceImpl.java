package com.soybean.admin.oss.service.impl;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.soybean.admin.data.entity.SysOssConfig;
import com.soybean.admin.data.mapper.SysOssConfigMapper;
import com.soybean.admin.oss.dto.FileInfo;
import com.soybean.admin.oss.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 阿里云OSS存储实现
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "file.storage", name = "type", havingValue = "aliyun")
@RequiredArgsConstructor
public class AliyunOssStorageServiceImpl implements FileStorageService {

    private final SysOssConfigMapper ossConfigMapper;
    private OSS ossClient;
    private SysOssConfig config;

    /**
     * 分片上传缓存
     */
    private final Map<String, ChunkUploadContext> chunkUploadCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // 加载默认配置
        loadDefaultConfig();
        if (config != null) {
            log.info("Aliyun OSS storage initialized with bucket: {}", config.getBucketName());
        } else {
            log.warn("No Aliyun OSS configuration found");
        }
    }

    private void loadDefaultConfig() {
        config = ossConfigMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysOssConfig>()
                .eq(SysOssConfig::getProvider, "aliyun")
                .eq(SysOssConfig::getIsDefault, "1")
                .eq(SysOssConfig::getStatus, "1")
                .last("LIMIT 1")
        );

        if (config != null) {
            ossClient = new OSSClientBuilder().build(
                config.getEndpoint(),
                config.getAccessKey(),
                config.getSecretKey()
            );
        }
    }

    @PreDestroy
    public void destroy() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    private OSS getOssClient() {
        if (ossClient == null) {
            throw new RuntimeException("OSS client not initialized");
        }
        return ossClient;
    }

    @Override
    public FileInfo upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = getFileExtension(originalFilename);
            String fileName = IdUtil.simpleUUID() + (suffix.isEmpty() ? "" : "." + suffix);

            // 按日期创建目录
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String pathPrefix = config.getPathPrefix();
            String objectKey = (pathPrefix == null || pathPrefix.isEmpty() ? "" : pathPrefix + "/")
                + datePath + "/" + fileName;

            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectResult result = getOssClient().putObject(config.getBucketName(), objectKey, inputStream, metadata);

            String fileMd5 = digestMd5(file.getInputStream());

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setOriginalName(originalFilename);
            fileInfo.setFileSuffix(suffix);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(file.getContentType());
            fileInfo.setConfigId(config.getId());
            fileInfo.setProvider("aliyun");
            fileInfo.setFilePath(objectKey);
            fileInfo.setUrl(getFileUrl(objectKey));
            fileInfo.setFileMd5(fileMd5);

            log.info("File uploaded to Aliyun OSS: {}", objectKey);
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to upload file to Aliyun OSS", e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    public FileInfo upload(InputStream inputStream, String path, String fileName) {
        try {
            String suffix = getFileExtension(fileName);
            String uniqueFileName = IdUtil.simpleUUID() + (suffix.isEmpty() ? "" : "." + suffix);

            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String pathPrefix = config.getPathPrefix();
            String objectKey = (pathPrefix == null || pathPrefix.isEmpty() ? "" : pathPrefix + "/")
                + datePath + "/" + uniqueFileName;

            // 读取全部内容以获取大小和MD5
            byte[] bytes = inputStream.readAllBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);

            getOssClient().putObject(config.getBucketName(), objectKey, bais, metadata);

            String fileMd5 = digestMd5(new ByteArrayInputStream(bytes));

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(uniqueFileName);
            fileInfo.setOriginalName(fileName);
            fileInfo.setFileSuffix(suffix);
            fileInfo.setFileSize((long) bytes.length);
            fileInfo.setProvider("aliyun");
            fileInfo.setConfigId(config.getId());
            fileInfo.setFilePath(objectKey);
            fileInfo.setUrl(getFileUrl(objectKey));
            fileInfo.setFileMd5(fileMd5);

            log.info("File uploaded to Aliyun OSS: {}", objectKey);
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to upload file from stream to Aliyun OSS", e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String initChunkUpload(String fileName, Long fileSize, String fileMd5, Integer chunkSize, Integer totalChunks) {
        String uploadId = IdUtil.simpleUUID();
        String suffix = getFileExtension(fileName);
        String objectName = IdUtil.simpleUUID() + (suffix.isEmpty() ? "" : "." + suffix);

        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String pathPrefix = config.getPathPrefix();
        String objectKey = (pathPrefix == null || pathPrefix.isEmpty() ? "" : pathPrefix + "/")
            + datePath + "/" + objectName;

        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(
            config.getBucketName(), objectKey);

        InitiateMultipartUploadResult result = getOssClient().initiateMultipartUpload(request);

        ChunkUploadContext context = new ChunkUploadContext();
        context.uploadId = result.getUploadId();
        context.objectKey = objectKey;
        context.fileName = fileName;
        context.fileSize = fileSize;
        context.fileMd5 = fileMd5;
        context.chunkSize = chunkSize;
        context.totalChunks = totalChunks;
        context.receivedChunks = 0;
        context.partETags = new java.util.ArrayList<>();

        chunkUploadCache.put(uploadId, context);
        log.info("Chunk upload initialized for Aliyun OSS: uploadId={}, objectKey={}", uploadId, objectKey);

        return uploadId;
    }

    @Override
    public boolean uploadChunk(String uploadId, Integer chunkNumber, MultipartFile file) {
        ChunkUploadContext context = chunkUploadCache.get(uploadId);
        if (context == null) {
            throw new RuntimeException("Upload session not found: " + uploadId);
        }

        try {
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(config.getBucketName());
            uploadPartRequest.setKey(context.objectKey);
            uploadPartRequest.setUploadId(context.uploadId);
            uploadPartRequest.setInputStream(file.getInputStream());
            uploadPartRequest.setPartSize(file.getSize());
            uploadPartRequest.setPartNumber(chunkNumber);

            UploadPartResult uploadPartResult = getOssClient().uploadPart(uploadPartRequest);
            context.partETags.add(uploadPartResult.getPartETag());
            context.receivedChunks++;

            log.debug("Chunk uploaded to Aliyun OSS: uploadId={}, part={}/{}", uploadId, chunkNumber, context.totalChunks);
            return true;
        } catch (Exception e) {
            log.error("Failed to upload chunk to Aliyun OSS", e);
            throw new RuntimeException("Chunk upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    public FileInfo completeChunkUpload(String uploadId) {
        ChunkUploadContext context = chunkUploadCache.get(uploadId);
        if (context == null) {
            throw new RuntimeException("Upload session not found: " + uploadId);
        }

        try {
            CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(
                config.getBucketName(),
                context.objectKey,
                context.uploadId,
                context.partETags
            );

            CompleteMultipartUploadResult result = getOssClient().completeMultipartUpload(completeRequest);

            // 清理缓存
            chunkUploadCache.remove(uploadId);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(context.objectKey.substring(context.objectKey.lastIndexOf("/") + 1));
            fileInfo.setOriginalName(context.fileName);
            fileInfo.setFileSuffix(getFileExtension(context.fileName));
            fileInfo.setFileSize(context.fileSize);
            fileInfo.setProvider("aliyun");
            fileInfo.setConfigId(config.getId());
            fileInfo.setFilePath(context.objectKey);
            fileInfo.setUrl(getFileUrl(context.objectKey));
            fileInfo.setFileMd5(context.fileMd5);

            log.info("Chunk upload completed for Aliyun OSS: uploadId={}, objectKey={}", uploadId, context.objectKey);
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to complete chunk upload to Aliyun OSS", e);
            throw new RuntimeException("Failed to merge chunks: " + e.getMessage(), e);
        }
    }

    @Override
    public void abortChunkUpload(String uploadId) {
        ChunkUploadContext context = chunkUploadCache.remove(uploadId);
        if (context != null) {
            AbortMultipartUploadRequest abortRequest = new AbortMultipartUploadRequest(
                config.getBucketName(),
                context.objectKey,
                context.uploadId
            );
            getOssClient().abortMultipartUpload(abortRequest);
            log.info("Chunk upload aborted for Aliyun OSS: uploadId={}", uploadId);
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            getOssClient().deleteObject(config.getBucketName(), filePath);
            log.info("File deleted from Aliyun OSS: {}", filePath);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete file from Aliyun OSS: {}", filePath, e);
            return false;
        }
    }

    @Override
    public int deleteBatch(List<String> filePaths) {
        try {
            List<String> keys = new java.util.ArrayList<>();
            for (String filePath : filePaths) {
                keys.add(filePath);
            }
            DeleteObjectsRequest request = new DeleteObjectsRequest(config.getBucketName()).withKeys(keys);
            DeleteObjectsResult result = getOssClient().deleteObjects(request);
            return result.getDeletedObjects().size();
        } catch (Exception e) {
            log.error("Failed to batch delete files from Aliyun OSS", e);
            return 0;
        }
    }

    @Override
    public InputStream download(String filePath) {
        try {
            OSSObject ossObject = getOssClient().getObject(config.getBucketName(), filePath);
            return ossObject.getObjectContent();
        } catch (Exception e) {
            log.error("Failed to download file from Aliyun OSS: {}", filePath, e);
            throw new RuntimeException("File download failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        String domain = config.getDomain();
        if (domain != null && !domain.isEmpty()) {
            return domain + "/" + filePath;
        }
        return "https://" + config.getBucketName() + "." + config.getEndpoint() + "/" + filePath;
    }

    @Override
    public FileInfo getFileInfo(String filePath) {
        try {
            OSSObject ossObject = getOssClient().getObject(config.getBucketName(), filePath);
            if (ossObject == null) {
                return null;
            }

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilePath(filePath);
            fileInfo.setFileSize(ossObject.getObjectMetadata().getContentLength());
            fileInfo.setProvider("aliyun");
            fileInfo.setUrl(getFileUrl(filePath));

            ossObject.close();
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to get file info from Aliyun OSS: {}", filePath, e);
            return null;
        }
    }

    @Override
    public boolean exists(String filePath) {
        try {
            return getOssClient().doesObjectExist(config.getBucketName(), filePath);
        } catch (Exception e) {
            log.error("Failed to check file existence in Aliyun OSS: {}", filePath, e);
            return false;
        }
    }

    @Override
    public String getStorageType() {
        return "aliyun";
    }

    @Override
    public String generateThumbnail(String filePath) {
        // TODO: 实现阿里云OSS图片处理服务生成缩略图
        // 阿里云OSS可以使用图片处理服务：?x-oss-process=image/resize,w_200
        return null;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }

    private String digestMd5(InputStream inputStream) throws Exception {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            md.update(buffer, 0, len);
        }
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 分片上传上下文
     */
    private static class ChunkUploadContext {
        String uploadId;
        String objectKey;
        String fileName;
        Long fileSize;
        String fileMd5;
        Integer chunkSize;
        Integer totalChunks;
        Integer receivedChunks;
        List<PartETag> partETags;
    }
}
