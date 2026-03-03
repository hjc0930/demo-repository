-- ============================================
-- 个人博客系统数据库建表脚本
-- MySQL 8.0+
-- ============================================

-- ============================================
-- 1. 用户表
-- ============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
    `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `introduction` VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
    `website` VARCHAR(255) DEFAULT NULL COMMENT '个人网站',
    `location` VARCHAR(100) DEFAULT NULL COMMENT '所在地',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色：USER-普通用户，ADMIN-管理员',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 2. 分类表
-- ============================================
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '分类描述',
    `icon` VARCHAR(100) DEFAULT NULL COMMENT '分类图标',
    `sort` INT DEFAULT 0 COMMENT '排序值，越小越靠前',
    `article_count` INT DEFAULT 0 COMMENT '文章数量',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    KEY `idx_status` (`status`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章分类表';

-- ============================================
-- 3. 标签表
-- ============================================
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '标签描述',
    `color` VARCHAR(20) DEFAULT NULL COMMENT '标签颜色',
    `article_count` INT DEFAULT 0 COMMENT '文章数量',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签表';

-- ============================================
-- 4. 文章表
-- ============================================
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title` VARCHAR(200) NOT NULL COMMENT '文章标题',
    `summary` VARCHAR(500) DEFAULT NULL COMMENT '文章摘要',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片URL',
    `content` MEDIUMTEXT NOT NULL COMMENT '文章内容（Markdown）',
    `content_html` MEDIUMTEXT DEFAULT NULL COMMENT '渲染后的HTML内容',
    `category_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '分类ID',
    `author_id` BIGINT UNSIGNED NOT NULL COMMENT '作者ID',
    `view_count` INT UNSIGNED DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT UNSIGNED DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT UNSIGNED DEFAULT 0 COMMENT '评论数',
    `collect_count` INT UNSIGNED DEFAULT 0 COMMENT '收藏数',
    `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    `is_featured` TINYINT DEFAULT 0 COMMENT '是否精选：0-否，1-是',
    `is_original` TINYINT DEFAULT 1 COMMENT '是否原创：0-转载，1-原创',
    `source_url` VARCHAR(255) DEFAULT NULL COMMENT '转载来源URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-草稿，1-已发布，2-已下架',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_author_id` (`author_id`),
    KEY `idx_status` (`status`),
    KEY `idx_is_top` (`is_top`),
    KEY `idx_publish_time` (`publish_time`),
    KEY `idx_create_time` (`create_time`),
    FULLTEXT KEY `ft_title_content` (`title`, `content`) WITH PARSER ngram
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- ============================================
-- 5. 文章-标签关联表
-- ============================================
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `article_id` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
    `tag_id` BIGINT UNSIGNED NOT NULL COMMENT '标签ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_article_tag` (`article_id`, `tag_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签关联表';

-- ============================================
-- 6. 评论表
-- ============================================
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `article_id` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '评论用户ID',
    `parent_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '父评论ID（一级评论为NULL）',
    `reply_to_user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '被回复用户ID',
    `content` VARCHAR(1000) NOT NULL COMMENT '评论内容',
    `like_count` INT UNSIGNED DEFAULT 0 COMMENT '点赞数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-待审核，1-已通过，2-已拒绝',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ============================================
-- 7. 点赞表
-- ============================================
DROP TABLE IF EXISTS `like_record`;
CREATE TABLE `like_record` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '点赞记录ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `target_id` BIGINT UNSIGNED NOT NULL COMMENT '目标ID（文章ID或评论ID）',
    `target_type` TINYINT NOT NULL COMMENT '目标类型：1-文章，2-评论',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-取消点赞，1-已点赞',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_target_type` (`user_id`, `target_id`, `target_type`),
    KEY `idx_target_id_type` (`target_id`, `target_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞记录表';

-- ============================================
-- 8. 收藏表
-- ============================================
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `article_id` BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
    `folder_name` VARCHAR(50) DEFAULT 'default' COMMENT '收藏夹名称',
    `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_article` (`user_id`, `article_id`),
    KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章收藏表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入默认管理员用户（密码：admin123，BCrypt加密后）
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `role`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 'admin@blog.com', 'ADMIN', 1),
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '测试用户', 'test@blog.com', 'USER', 1);

-- 插入默认分类
INSERT INTO `category` (`name`, `description`, `icon`, `sort`) VALUES
('后端技术', 'Java、Spring、数据库等后端技术分享', 'code', 1),
('前端开发', 'Vue、React、前端工程化等', 'web', 2),
('运维部署', 'Docker、K8s、Linux等运维相关', 'server', 3),
('算法学习', '数据结构与算法学习笔记', 'algorithm', 4),
('生活随笔', '生活感悟与随笔记录', 'coffee', 5);

-- 插入默认标签
INSERT INTO `tag` (`name`, `description`, `color`) VALUES
('Java', 'Java编程语言', '#f89820'),
('Spring Boot', 'Spring Boot框架', '#6db33f'),
('MyBatis', 'MyBatis持久层框架', '#d46a3c'),
('Redis', 'Redis缓存数据库', '#dc382d'),
('MySQL', 'MySQL关系型数据库', '#00758f'),
('Vue', 'Vue前端框架', '#42b883'),
('Docker', 'Docker容器技术', '#2496ed'),
('算法', '算法与数据结构', '#8b5cf6');

-- 插入示例文章
INSERT INTO `article` (`title`, `summary`, `content`, `category_id`, `author_id`, `status`, `publish_time`) VALUES
('欢迎来到我的博客', '这是第一篇示例文章，欢迎来到我的个人博客系统。本系统基于 Spring Boot + MySQL + Redis 构建。', '# 欢迎来到我的博客\n\n这是第一篇示例文章，系统主要功能包括：\n\n- 用户注册登录\n- 文章发布管理\n- 评论互动\n- 分类标签\n\n感谢访问！', 1, 1, 1, NOW()),
('Spring Boot 入门教程', '从零开始学习 Spring Boot 框架的使用。', '# Spring Boot 入门教程\n\n## 什么是 Spring Boot\n\nSpring Boot 是基于 Spring 框架的快速开发工具...\n\n## 快速开始\n\n```java\n@SpringBootApplication\npublic class Application {\n    public static void main(String[] args) {\n        SpringApplication.run(Application.class, args);\n    }\n}\n```', 1, 1, 1, NOW());

-- ============================================
-- 视图定义（可选）
-- ============================================

-- 文章列表视图（包含分类和作者信息）
CREATE OR REPLACE VIEW `v_article_list` AS
SELECT
    a.id,
    a.title,
    a.summary,
    a.cover_image,
    a.view_count,
    a.like_count,
    a.comment_count,
    a.is_top,
    a.is_featured,
    a.publish_time,
    a.create_time,
    c.id AS category_id,
    c.name AS category_name,
    c.icon AS category_icon,
    u.id AS author_id,
    u.username,
    u.nickname,
    u.avatar
FROM article a
LEFT JOIN category c ON a.category_id = c.id AND c.is_deleted = 0
LEFT JOIN user u ON a.author_id = u.id AND u.is_deleted = 0
WHERE a.is_deleted = 0;
