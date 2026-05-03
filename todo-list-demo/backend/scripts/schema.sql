CREATE DATABASE IF NOT EXISTS todo_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE todo_db;

CREATE TABLE IF NOT EXISTS todos (
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255)    NOT NULL,
    description TEXT            DEFAULT '',
    status      TINYINT         NOT NULL DEFAULT 0 COMMENT '0=待办, 1=进行中, 2=已完成',
    priority    TINYINT         NOT NULL DEFAULT 1 COMMENT '0=低, 1=中, 2=高',
    due_date    DATETIME        NULL,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  DATETIME        NULL,
    INDEX idx_deleted_at (deleted_at),
    INDEX idx_due_date (due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
