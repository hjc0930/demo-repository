package com.soybean.admin.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OSS配置表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oss_config")
public class SysOssConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 存储提供商（local-本地, aliyun-阿里云, tencent-腾讯云, minio-MinIO）
     */
    private String provider;

    /**
     * 访问端点
     */
    private String endpoint;

    /**
     * 访问密钥ID
     */
    private String accessKey;

    /**
     * 访问密钥Secret
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 区域
     */
    private String region;

    /**
     * 存储路径前缀
     */
    private String pathPrefix;

    /**
     * 访问域名
     */
    private String domain;

    /**
     * 是否默认（0-否 1-是）
     */
    private String isDefault;

    /**
     * 状态（0-禁用 1-启用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
