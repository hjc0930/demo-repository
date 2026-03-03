package com.soybean.admin.oss.service.impl;

import cn.hutool.core.util.IdUtil;
import com.soybean.admin.data.entity.SysOssConfig;
import com.soybean.admin.data.mapper.SysOssConfigMapper;
import com.soybean.admin.oss.dto.FileInfo;
import com.soybean.admin.oss.service.FileStorageService;
import io.minio.*;
import io.minio.messages.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MinIO存储实现
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "file.storage", name = "type", havingValue = "minio")
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements FileStorageService {

    private final SysOssConfigMapper ossConfigMapper;
    private MinioClient minioClient;
    private SysOssConfig config;

    /**
     * 分片上传缓存
     */
    private final Map<String, ChunkUploadContext> chunkUploadCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        loadDefaultConfig();
        if (config != null) {
            log.info("MinIO storage initialized with bucket: {}", config.getBucketName());
        } else {
            log.warn("No MinIO configuration found");
        }
    }

    private void loadDefaultConfig() {
        config = ossConfigMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysOssConfig>()
                .eq(SysOssConfig::getProvider, "minio")
                .eq(SysOssConfig::getIsDefault, "1")
                .eq(SysOssConfig::getStatus, "1")
                .last("LIMIT 1")
        );

        if (config != null) {
            minioClient = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();

            // 确保存储桶存在
            try {
                boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(config.getBucketName())
                    .build());
                if (!found) {
                    minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(config.getBucketName())
                        .build());
                    log.info("Created new MinIO bucket: {}", config.getBucketName());
                }
            } catch (Exception e) {
                log.error("Failed to check/create MinIO bucket", e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        // MinioClient不需要显式关闭
    }

    private MinioClient getMinioClient() {
        if (minioClient == null) {
            throw new RuntimeException("MinIO client not initialized");
        }
        return minioClient;
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

            getMinioClient().putObject(PutObjectArgs.builder()
                .bucket(config.getBucketName())
                .object(objectKey)
                .stream(inputStream, file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

            String fileMd5 = calculateMd5(file.getInputStream());

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setOriginalName(originalFilename);
            fileInfo.setFileSuffix(suffix);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(file.getContentType());
            fileInfo.setConfigId(config.getId());
            fileInfo.setProvider("minio");
            fileInfo.setFilePath(objectKey);
            fileInfo.setUrl(getFileUrl(objectKey));
            fileInfo.setFileMd5(fileMd5);

            log.info("File uploaded to MinIO: {}", objectKey);
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to upload file to MinIO", e);
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

            // 读取全部内容
            byte[] bytes = inputStream.readAllBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

            getMinioClient().putObject(PutObjectArgs.builder()
                .bucket(config.getBucketName())
                .object(objectKey)
                .stream(bais, bytes.length, -1)
                .build());

            String fileMd5 = calculateMd5(new ByteArrayInputStream(bytes));

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(uniqueFileName);
            fileInfo.setOriginalName(fileName);
            fileInfo.setFileSuffix(suffix);
            fileInfo.setFileSize((long) bytes.length);
            fileInfo.setProvider("minio");
            fileInfo.setConfigId(config.getId());
            fileInfo.setFilePath(objectKey);
            fileInfo.setUrl(getFileUrl(objectKey));
            fileInfo.setFileMd5(fileMd5);

            log.info("File uploaded to MinIO: {}", objectKey);
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to upload file from stream to MinIO", e);
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

        ChunkUploadContext context = new ChunkUploadContext();
        context.uploadId = uploadId;
        context.objectKey = objectKey;
        context.fileName = fileName;
        context.fileSize = fileSize;
        context.fileMd5 = fileMd5;
        context.chunkSize = chunkSize;
        context.totalChunks = totalChunks;
        context.receivedChunks = 0;
        context.uploadedParts = new ArrayList<>();

        chunkUploadCache.put(uploadId, context);
        log.info("Chunk upload initialized for MinIO: uploadId={}, objectKey={}", uploadId, objectKey);

        return uploadId;
    }

    @Override
    public boolean uploadChunk(String uploadId, Integer chunkNumber, MultipartFile file) {
        ChunkUploadContext context = chunkUploadCache.get(uploadId);
        if (context == null) {
            throw new RuntimeException("Upload session not found: " + uploadId);
        }

        try {
            // MinIO的分片上传需要先创建uploadId
            String minioUploadId = context.minioUploadId;
            if (minioUploadId == null) {
                CreateMultipartUploadResponse response = getMinioClient().createMultipartUpload(
                    config.getBucketName(),
                    null,
                    context.objectKey,
                    null,
                    null
                );
                minioUploadId = response.result().uploadId();
                context.minioUploadId = minioUploadId;
            }

            UploadPartResponse partResponse = getMinioClient().uploadPart(
                config.getBucketName(),
                null,
                context.objectKey,
                minioUploadId,
                chunkNumber,
                file.getInputStream(),
                file.getSize(),
                null
            );

            context.uploadedParts.add(new Part(chunkNumber, partResponse.etag()));
            context.receivedChunks++;

            log.debug("Chunk uploaded to MinIO: uploadId={}, part={}/{}", uploadId, chunkNumber, context.totalChunks);
            return true;
        } catch (Exception e) {
            log.error("Failed to upload chunk to MinIO", e);
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
            ObjectWriteResponse response = getMinioClient().completeMultipartUpload(
                config.getBucketName(),
                null,
                context.objectKey,
                context.minioUploadId,
                context.uploadedParts,
                null,
                null
            );

            // 清理缓存
            chunkUploadCache.remove(uploadId);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(context.objectKey.substring(context.objectKey.lastIndexOf("/") + 1));
            fileInfo.setOriginalName(context.fileName);
            fileInfo.setFileSuffix(getFileExtension(context.fileName));
            fileInfo.setFileSize(context.fileSize);
            fileInfo.setProvider("minio");
            fileInfo.setConfigId(config.getId());
            fileInfo.setFilePath(context.objectKey);
            fileInfo.setUrl(getFileUrl(context.objectKey));
            fileInfo.setFileMd5(context.fileMd5);

            log.info("Chunk upload completed for MinIO: uploadId={}, objectKey={}", uploadId, context.objectKey);
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to complete chunk upload to MinIO", e);
            throw new RuntimeException("Failed to merge chunks: " + e.getMessage(), e);
        }
    }

    @Override
    public void abortChunkUpload(String uploadId) {
        ChunkUploadContext context = chunkUploadCache.remove(uploadId);
        if (context != null && context.minioUploadId != null) {
            try {
                getMinioClient().abortMultipartUpload(
                    config.getBucketName(),
                    null,
                    context.objectKey,
                    context.minioUploadId
                );
                log.info("Chunk upload aborted for MinIO: uploadId={}", uploadId);
            } catch (Exception e) {
                log.error("Failed to abort chunk upload for MinIO", e);
            }
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            getMinioClient().removeObject(RemoveObjectArgs.builder()
                .bucket(config.getBucketName())
                .object(filePath)
                .build());
            log.info("File deleted from MinIO: {}", filePath);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete file from MinIO: {}", filePath, e);
            return false;
        }
    }

    @Override
    public int deleteBatch(List<String> filePaths) {
        int count = 0;
        for (String filePath : filePaths) {
            if (delete(filePath)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public InputStream download(String filePath) {
        try {
            return getMinioClient().getObject(GetObjectArgs.builder()
                .bucket(config.getBucketName())
                .object(filePath)
                .build());
        } catch (Exception e) {
            log.error("Failed to download file from MinIO: {}", filePath, e);
            throw new RuntimeException("File download failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        String domain = config.getDomain();
        if (domain != null && !domain.isEmpty()) {
            return domain + "/" + filePath;
        }
        return config.getEndpoint() + "/" + config.getBucketName() + "/" + filePath;
    }

    @Override
    public FileInfo getFileInfo(String filePath) {
        try {
            StatObjectResponse stat = getMinioClient().statObject(StatObjectArgs.builder()
                .bucket(config.getBucketName())
                .object(filePath)
                .build());

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFilePath(filePath);
            fileInfo.setFileSize(stat.size());
            fileInfo.setProvider("minio");
            fileInfo.setUrl(getFileUrl(filePath));

            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to get file info from MinIO: {}", filePath, e);
            return null;
        }
    }

    @Override
    public boolean exists(String filePath) {
        try {
            getMinioClient().statObject(StatObjectArgs.builder()
                .bucket(config.getBucketName())
                .object(filePath)
                .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getStorageType() {
        return "minio";
    }

    @Override
    public String generateThumbnail(String filePath) {
        // TODO: 实现MinIO缩略图生成
        return null;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }

    private String calculateMd5(InputStream inputStream) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
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
        String minioUploadId;
        String objectKey;
        String fileName;
        Long fileSize;
        String fileMd5;
        Integer chunkSize;
        Integer totalChunks;
        Integer receivedChunks;
        List<Part> uploadedParts;
    }
}
