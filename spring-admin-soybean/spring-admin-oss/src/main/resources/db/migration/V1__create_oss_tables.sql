-- OSS配置表
CREATE TABLE IF NOT EXISTS sys_oss_config (
    id BIGINT PRIMARY KEY,
    config_name VARCHAR(100) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    endpoint VARCHAR(255),
    access_key VARCHAR(255),
    secret_key VARCHAR(255),
    bucket_name VARCHAR(100),
    region VARCHAR(100),
    path_prefix VARCHAR(200),
    domain VARCHAR(255),
    is_default CHAR(1) DEFAULT '0',
    status CHAR(1) DEFAULT '1',
    create_by VARCHAR(64),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500),
    del_flag CHAR(1) DEFAULT '0'
);

COMMENT ON TABLE sys_oss_config IS 'OSS配置表';
COMMENT ON COLUMN sys_oss_config.id IS '主键ID';
COMMENT ON COLUMN sys_oss_config.config_name IS '配置名称';
COMMENT ON COLUMN sys_oss_config.provider IS '存储提供商（local-本地, aliyun-阿里云, tencent-腾讯云, minio-MinIO）';
COMMENT ON COLUMN sys_oss_config.endpoint IS '访问端点';
COMMENT ON COLUMN sys_oss_config.access_key IS '访问密钥ID';
COMMENT ON COLUMN sys_oss_config.secret_key IS '访问密钥Secret';
COMMENT ON COLUMN sys_oss_config.bucket_name IS '存储桶名称';
COMMENT ON COLUMN sys_oss_config.region IS '区域';
COMMENT ON COLUMN sys_oss_config.path_prefix IS '存储路径前缀';
COMMENT ON COLUMN sys_oss_config.domain IS '访问域名';
COMMENT ON COLUMN sys_oss_config.is_default IS '是否默认（0-否 1-是）';
COMMENT ON COLUMN sys_oss_config.status IS '状态（0-禁用 1-启用）';

-- OSS文件表
CREATE TABLE IF NOT EXISTS sys_oss (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(100) NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    file_suffix VARCHAR(50),
    file_size BIGINT,
    file_type VARCHAR(100),
    config_id BIGINT,
    provider VARCHAR(50),
    file_path VARCHAR(500),
    url VARCHAR(500),
    file_md5 VARCHAR(32),
    thumbnail_url VARCHAR(500),
    user_id BIGINT,
    tenant_id VARCHAR(20),
    create_by VARCHAR(64),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500),
    del_flag CHAR(1) DEFAULT '0'
);

COMMENT ON TABLE sys_oss IS 'OSS文件表';
COMMENT ON COLUMN sys_oss.id IS '主键ID';
COMMENT ON COLUMN sys_oss.file_name IS '文件名';
COMMENT ON COLUMN sys_oss.original_name IS '原始文件名';
COMMENT ON COLUMN sys_oss.file_suffix IS '文件后缀';
COMMENT ON COLUMN sys_oss.file_size IS '文件大小（字节）';
COMMENT ON COLUMN sys_oss.file_type IS '文件类型（MIME类型）';
COMMENT ON COLUMN sys_oss.config_id IS '存储配置ID';
COMMENT ON COLUMN sys_oss.provider IS '存储提供商';
COMMENT ON COLUMN sys_oss.file_path IS '存储路径';
COMMENT ON COLUMN sys_oss.url IS '访问URL';
COMMENT ON COLUMN sys_oss.file_md5 IS '文件MD5';
COMMENT ON COLUMN sys_oss.thumbnail_url IS '缩略图URL';
COMMENT ON COLUMN sys_oss.user_id IS '上传用户ID';
COMMENT ON COLUMN sys_oss.tenant_id IS '租户ID';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_sys_oss_config_provider ON sys_oss_config(provider);
CREATE INDEX IF NOT EXISTS idx_sys_oss_config_default ON sys_oss_config(is_default);
CREATE INDEX IF NOT EXISTS idx_sys_oss_config_status ON sys_oss_config(status);
CREATE INDEX IF NOT EXISTS idx_sys_oss_file_md5 ON sys_oss(file_md5);
CREATE INDEX IF NOT EXISTS idx_sys_oss_tenant_id ON sys_oss(tenant_id);
CREATE INDEX IF NOT EXISTS idx_sys_oss_user_id ON sys_oss(user_id);

-- 插入默认本地存储配置
INSERT INTO sys_oss_config (id, config_name, provider, endpoint, bucket_name, path_prefix, is_default, status, create_by)
VALUES (1, '本地存储', 'local', '', '', '', '1', '1', 'system')
ON CONFLICT (id) DO NOTHING;
