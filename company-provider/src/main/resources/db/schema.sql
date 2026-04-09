CREATE DATABASE IF NOT EXISTS `company_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `company_test`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username`    VARCHAR(64)  NOT NULL COMMENT 'username',
    `email`       VARCHAR(128) DEFAULT NULL COMMENT 'email',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT 'phone',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT 'status: 1-enabled, 0-disabled',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT 'logical delete flag',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='user table';
