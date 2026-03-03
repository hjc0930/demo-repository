package com.soybean.admin.oss.config;

import com.soybean.admin.oss.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件存储配置
 */
@Slf4j
@Configuration
public class FileStorageConfiguration {

    /**
     * 如果没有配置具体的存储实现，使用本地存储
     */
    @Bean
    @ConditionalOnMissingBean(FileStorageService.class)
    public FileStorageService defaultFileStorageService() {
        log.info("No file storage service configured, using default local storage");
        return new DefaultFileStorageService();
    }

    /**
     * 默认文件存储服务（本地存储）
     */
    public static class DefaultFileStorageService implements FileStorageService {
        @Override
        public org.springframework.web.multipart.MultipartFile upload(org.springframework.web.multipart.MultipartFile file) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public com.soybean.admin.oss.dto.FileInfo upload(java.io.InputStream inputStream, String path, String fileName) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public String initChunkUpload(String fileName, Long fileSize, String fileMd5, Integer chunkSize, Integer totalChunks) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public boolean uploadChunk(String uploadId, Integer chunkNumber, org.springframework.web.multipart.MultipartFile file) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public com.soybean.admin.oss.dto.FileInfo completeChunkUpload(String uploadId) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public void abortChunkUpload(String uploadId) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public boolean delete(String filePath) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public int deleteBatch(java.util.List<String> filePaths) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public java.io.InputStream download(String filePath) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public String getFileUrl(String filePath) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public com.soybean.admin.oss.dto.FileInfo getFileInfo(String filePath) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public boolean exists(String filePath) {
            throw new UnsupportedOperationException("File storage not configured");
        }

        @Override
        public String getStorageType() {
            return "none";
        }

        @Override
        public String generateThumbnail(String filePath) {
            throw new UnsupportedOperationException("File storage not configured");
        }
    }
}
