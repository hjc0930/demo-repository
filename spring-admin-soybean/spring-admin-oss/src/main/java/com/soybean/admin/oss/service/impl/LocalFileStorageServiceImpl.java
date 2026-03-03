package com.soybean.admin.oss.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.soybean.admin.oss.dto.FileInfo;
import com.soybean.admin.oss.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地文件存储实现
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "file.storage", name = "type", havingValue = "local", matchIfMissing = true)
@RequiredArgsConstructor
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Value("${file.storage.local.path:uploads}")
    private String basePath;

    @Value("${file.storage.local.domain:http://localhost:8080}")
    private String domain;

    @Value("${file.storage.local.path-prefix:}")
    private String pathPrefix;

    /**
     * 分片上传缓存
     */
    private final Map<String, ChunkUploadContext> chunkUploadCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {
            Path path = Paths.get(basePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            log.info("Local file storage initialized with base path: {}", basePath);
        } catch (Exception e) {
            log.error("Failed to initialize local file storage", e);
        }
    }

    @Override
    public FileInfo upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = FileUtil.getSuffix(originalFilename);
            String fileName = IdUtil.simpleUUID() + (suffix.isEmpty() ? "" : "." + suffix);

            // 按日期创建目录
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = (pathPrefix.isEmpty() ? "" : pathPrefix + "/") + datePath + "/" + fileName;
            String fullPath = basePath + File.separator + relativePath.replace("/", File.separator);

            File targetFile = new File(fullPath);
            FileUtil.mkParentDirs(targetFile);
            file.transferTo(targetFile);

            String fileMd5 = DigestUtil.md5Hex(new FileInputStream(targetFile));

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setOriginalName(originalFilename);
            fileInfo.setFileSuffix(suffix);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(file.getContentType());
            fileInfo.setProvider("local");
            fileInfo.setFilePath(relativePath);
            fileInfo.setUrl(domain + "/" + relativePath);
            fileInfo.setFileMd5(fileMd5);

            log.info("File uploaded successfully: {}", relativePath);
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to upload file", e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    public FileInfo upload(InputStream inputStream, String path, String fileName) {
        try {
            String suffix = FileUtil.getSuffix(fileName);
            String uniqueFileName = IdUtil.simpleUUID() + (suffix.isEmpty() ? "" : "." + suffix);

            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = (pathPrefix.isEmpty() ? "" : pathPrefix + "/") + datePath + "/" + uniqueFileName;
            String fullPath = basePath + File.separator + relativePath.replace("/", File.separator);

            File targetFile = new File(fullPath);
            FileUtil.mkParentDirs(targetFile);
            FileUtil.writeFromStream(inputStream, targetFile);

            String fileMd5 = DigestUtil.md5Hex(new FileInputStream(targetFile));

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(uniqueFileName);
            fileInfo.setOriginalName(fileName);
            fileInfo.setFileSuffix(suffix);
            fileInfo.setFileSize(targetFile.length());
            fileInfo.setProvider("local");
            fileInfo.setFilePath(relativePath);
            fileInfo.setUrl(domain + "/" + relativePath);
            fileInfo.setFileMd5(fileMd5);

            log.info("File uploaded successfully: {}", relativePath);
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to upload file from stream", e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String initChunkUpload(String fileName, Long fileSize, String fileMd5, Integer chunkSize, Integer totalChunks) {
        String uploadId = IdUtil.simpleUUID();
        ChunkUploadContext context = new ChunkUploadContext();
        context.uploadId = uploadId;
        context.fileName = fileName;
        context.fileSize = fileSize;
        context.fileMd5 = fileMd5;
        context.chunkSize = chunkSize;
        context.totalChunks = totalChunks;
        context.receivedChunks = 0;

        // 创建临时目录
        String tempDir = basePath + File.separator + "temp" + File.separator + uploadId;
        context.tempDir = tempDir;
        FileUtil.mkdir(tempDir);

        chunkUploadCache.put(uploadId, context);
        log.info("Chunk upload initialized: uploadId={}, fileName={}, totalChunks={}", uploadId, fileName, totalChunks);

        return uploadId;
    }

    @Override
    public boolean uploadChunk(String uploadId, Integer chunkNumber, MultipartFile file) {
        ChunkUploadContext context = chunkUploadCache.get(uploadId);
        if (context == null) {
            throw new RuntimeException("Upload session not found: " + uploadId);
        }

        try {
            String chunkFileName = context.tempDir + File.separator + chunkNumber;
            file.transferTo(new File(chunkFileName));

            context.receivedChunks++;

            log.debug("Chunk uploaded: uploadId={}, chunk={}/{}", uploadId, chunkNumber, context.totalChunks);
            return true;
        } catch (Exception e) {
            log.error("Failed to upload chunk", e);
            throw new RuntimeException("Chunk upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    public FileInfo completeChunkUpload(String uploadId) {
        ChunkUploadContext context = chunkUploadCache.get(uploadId);
        if (context == null) {
            throw new RuntimeException("Upload session not found: " + uploadId);
        }

        if (context.receivedChunks < context.totalChunks) {
            throw new RuntimeException("Not all chunks received: " + context.receivedChunks + "/" + context.totalChunks);
        }

        try {
            String suffix = FileUtil.getSuffix(context.fileName);
            String finalFileName = IdUtil.simpleUUID() + (suffix.isEmpty() ? "" : "." + suffix);
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = (pathPrefix.isEmpty() ? "" : pathPrefix + "/") + datePath + "/" + finalFileName;
            String fullPath = basePath + File.separator + relativePath.replace("/", File.separator);

            File finalFile = new File(fullPath);
            FileUtil.mkParentDirs(finalFile);

            // 合并分片
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(finalFile)) {
                for (int i = 1; i <= context.totalChunks; i++) {
                    File chunkFile = new File(context.tempDir + File.separator + i);
                    Files.copy(chunkFile.toPath(), fos);
                }
            }

            // 验证MD5
            String actualMd5 = DigestUtil.md5Hex(new FileInputStream(finalFile));
            if (!actualMd5.equals(context.fileMd5)) {
                FileUtil.del(finalFile);
                throw new RuntimeException("File MD5 mismatch after merge");
            }

            // 清理临时文件
            FileUtil.del(new File(context.tempDir));
            chunkUploadCache.remove(uploadId);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(finalFileName);
            fileInfo.setOriginalName(context.fileName);
            fileInfo.setFileSuffix(suffix);
            fileInfo.setFileSize(context.fileSize);
            fileInfo.setProvider("local");
            fileInfo.setFilePath(relativePath);
            fileInfo.setUrl(domain + "/" + relativePath);
            fileInfo.setFileMd5(context.fileMd5);

            log.info("Chunk upload completed: uploadId={}, finalPath={}", uploadId, relativePath);
            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to complete chunk upload", e);
            throw new RuntimeException("Failed to merge chunks: " + e.getMessage(), e);
        }
    }

    @Override
    public void abortChunkUpload(String uploadId) {
        ChunkUploadContext context = chunkUploadCache.remove(uploadId);
        if (context != null) {
            FileUtil.del(new File(context.tempDir));
            log.info("Chunk upload aborted: uploadId={}", uploadId);
        }
    }

    @Override
    public boolean delete(String filePath) {
        try {
            String fullPath = basePath + File.separator + filePath.replace("/", File.separator);
            boolean result = FileUtil.del(fullPath);
            log.info("File deleted: {}, result={}", filePath, result);
            return result;
        } catch (Exception e) {
            log.error("Failed to delete file: {}", filePath, e);
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
            String fullPath = basePath + File.separator + filePath.replace("/", File.separator);
            return new FileInputStream(fullPath);
        } catch (Exception e) {
            log.error("Failed to download file: {}", filePath, e);
            throw new RuntimeException("File download failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        return domain + "/" + filePath;
    }

    @Override
    public FileInfo getFileInfo(String filePath) {
        try {
            String fullPath = basePath + File.separator + filePath.replace("/", File.separator);
            File file = new File(fullPath);
            if (!file.exists()) {
                return null;
            }

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(file.getName());
            fileInfo.setFilePath(filePath);
            fileInfo.setFileSize(file.length());
            fileInfo.setProvider("local");
            fileInfo.setUrl(domain + "/" + filePath);

            return fileInfo;
        } catch (Exception e) {
            log.error("Failed to get file info: {}", filePath, e);
            return null;
        }
    }

    @Override
    public boolean exists(String filePath) {
        String fullPath = basePath + File.separator + filePath.replace("/", File.separator);
        return FileUtil.exist(fullPath);
    }

    @Override
    public String getStorageType() {
        return "local";
    }

    @Override
    public String generateThumbnail(String filePath) {
        // TODO: 实现缩略图生成
        return null;
    }

    /**
     * 分片上传上下文
     */
    private static class ChunkUploadContext {
        String uploadId;
        String fileName;
        Long fileSize;
        String fileMd5;
        Integer chunkSize;
        Integer totalChunks;
        Integer receivedChunks;
        String tempDir;
    }
}
