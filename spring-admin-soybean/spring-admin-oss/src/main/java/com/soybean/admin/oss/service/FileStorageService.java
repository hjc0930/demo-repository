package com.soybean.admin.oss.service;

import org.springframework.web.multipart.MultipartFile;
import com.soybean.admin.oss.dto.FileInfo;

import java.io.InputStream;
import java.util.List;

/**
 * 文件存储服务接口
 */
public interface FileStorageService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件信息
     */
    FileInfo upload(MultipartFile file);

    /**
     * 上传文件流
     *
     * @param inputStream 文件流
     * @param path        存储路径
     * @param fileName    文件名
     * @return 文件信息
     */
    FileInfo upload(InputStream inputStream, String path, String fileName);

    /**
     * 分片上传-初始化
     *
     * @param fileName    文件名
     * @param fileSize    文件大小
     * @param fileMd5     文件MD5
     * @param chunkSize   分片大小
     * @param totalChunks 总分片数
     * @return 上传ID
     */
    String initChunkUpload(String fileName, Long fileSize, String fileMd5, Integer chunkSize, Integer totalChunks);

    /**
     * 分片上传-上传分片
     *
     * @param uploadId 上传ID
     * @param chunkNumber 分片序号（从1开始）
     * @param file 分片文件
     * @return 是否成功
     */
    boolean uploadChunk(String uploadId, Integer chunkNumber, MultipartFile file);

    /**
     * 分片上传-完成合并
     *
     * @param uploadId 上传ID
     * @return 文件信息
     */
    FileInfo completeChunkUpload(String uploadId);

    /**
     * 取消分片上传
     *
     * @param uploadId 上传ID
     */
    void abortChunkUpload(String uploadId);

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否成功
     */
    boolean delete(String filePath);

    /**
     * 批量删除文件
     *
     * @param filePaths 文件路径列表
     * @return 成功删除的数量
     */
    int deleteBatch(List<String> filePaths);

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @return 文件流
     */
    InputStream download(String filePath);

    /**
     * 获取文件访问URL
     *
     * @param filePath 文件路径
     * @return 访问URL
     */
    String getFileUrl(String filePath);

    /**
     * 获取文件信息
     *
     * @param filePath 文件路径
     * @return 文件信息
     */
    FileInfo getFileInfo(String filePath);

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    boolean exists(String filePath);

    /**
     * 获取存储类型
     *
     * @return 存储类型
     */
    String getStorageType();

    /**
     * 生成缩略图
     *
     * @param filePath 文件路径
     * @return 缩略图URL
     */
    String generateThumbnail(String filePath);
}
