-- 数据库表结构初始化脚本 V1
-- 根据实体类自动生成的DDL SQL

-- 禁用外键约束检查
SET FOREIGN_KEY_CHECKS = 0;

-- 电影分类表
CREATE TABLE IF NOT EXISTS `categories` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `parent_id` BIGINT,
  `create_at` DATETIME,
  `update_at` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_name` (`name`),
  KEY `idx_parent_id` (`parent_id`),
  CONSTRAINT `fk_category_parent` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 角色表
CREATE TABLE IF NOT EXISTS `roles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(255),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100),
  `phone` VARCHAR(20),
  `avatar` VARCHAR(255),
  `status` INT NOT NULL DEFAULT 1,
  `member_level` INT DEFAULT 0,
  `register_time` DATETIME,
  `last_login_time` DATETIME,
  `update_at` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 演员表
CREATE TABLE IF NOT EXISTS `actors` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `original_name` VARCHAR(100),
  `avatar_url` VARCHAR(255),
  `description` TEXT,
  `create_at` DATETIME,
  `update_at` DATETIME,
  PRIMARY KEY (`id`),
  KEY `idx_actor_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 导演表
CREATE TABLE IF NOT EXISTS `directors` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `original_name` VARCHAR(100),
  `avatar_url` VARCHAR(255),
  `description` TEXT,
  `create_at` DATETIME,
  `update_at` DATETIME,
  PRIMARY KEY (`id`),
  KEY `idx_director_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 影片表
CREATE TABLE IF NOT EXISTS `films` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `type` INT,
  `title` VARCHAR(200) NOT NULL,
  `original_title` VARCHAR(200),
  `rating` DOUBLE,
  `region` VARCHAR(100),
  `language` VARCHAR(100),
  `description` TEXT,
  `category_id` BIGINT,
  `poster_url` VARCHAR(255),
  `banner_url` VARCHAR(255),
  `video_url` VARCHAR(255),
  `air_date` DATE,
  `status` INT NOT NULL DEFAULT 1,
  `create_at` DATETIME NOT NULL,
  `update_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_air_date` (`air_date`),
  KEY `idx_status` (`status`),
  KEY `idx_title` (`title`),
  CONSTRAINT `fk_film_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 季表
CREATE TABLE IF NOT EXISTS `seasons` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `film_id` BIGINT NOT NULL,
  `season_number` INT,
  `create_at` DATETIME,
  `update_at` DATETIME,
  PRIMARY KEY (`id`),
  KEY `idx_film_id` (`film_id`),
  CONSTRAINT `fk_season_film` FOREIGN KEY (`film_id`) REFERENCES `films` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 剧集表
CREATE TABLE IF NOT EXISTS `episodes` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `season_id` BIGINT,
  `film_id` BIGINT NOT NULL,
  `episode_number` INT,
  `video_url` VARCHAR(255),
  `create_at` DATETIME,
  `update_at` DATETIME,
  PRIMARY KEY (`id`),
  KEY `idx_film_id` (`film_id`),
  KEY `idx_season_id` (`season_id`),
  CONSTRAINT `fk_episode_film` FOREIGN KEY (`film_id`) REFERENCES `films` (`id`),
  CONSTRAINT `fk_episode_season` FOREIGN KEY (`season_id`) REFERENCES `seasons` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 电影演员关联表
CREATE TABLE IF NOT EXISTS `film_actors` (
  `film_id` BIGINT NOT NULL,
  `actor_id` BIGINT NOT NULL,
  PRIMARY KEY (`film_id`, `actor_id`),
  KEY `idx_actor_id` (`actor_id`),
  CONSTRAINT `fk_film_actor_film` FOREIGN KEY (`film_id`) REFERENCES `films` (`id`),
  CONSTRAINT `fk_film_actor_actor` FOREIGN KEY (`actor_id`) REFERENCES `actors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 电影导演关联表
CREATE TABLE IF NOT EXISTS `film_directors` (
  `film_id` BIGINT NOT NULL,
  `director_id` BIGINT NOT NULL,
  PRIMARY KEY (`film_id`, `director_id`),
  KEY `idx_director_id` (`director_id`),
  CONSTRAINT `fk_film_director_film` FOREIGN KEY (`film_id`) REFERENCES `films` (`id`),
  CONSTRAINT `fk_film_director_director` FOREIGN KEY (`director_id`) REFERENCES `directors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 播放历史表
CREATE TABLE IF NOT EXISTS `play_histories` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `episode_id` BIGINT NOT NULL,
  `play_position` INT DEFAULT 0,
  `duration` INT DEFAULT 0,
  `watch_status` INT DEFAULT 0,
  `last_play_time` DATETIME,
  `create_at` DATETIME,
  `update_at` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_episode` (`user_id`, `episode_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_episode_id` (`episode_id`),
  CONSTRAINT `fk_play_history_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_play_history_episode` FOREIGN KEY (`episode_id`) REFERENCES `episodes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户剧集收藏表
CREATE TABLE IF NOT EXISTS `favorites` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `film_id` BIGINT NOT NULL,
  `create_at` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_film` (`user_id`, `film_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_film_id` (`film_id`),
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_favorite_film` FOREIGN KEY (`film_id`) REFERENCES `films` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 评论表
CREATE TABLE IF NOT EXISTS `comments` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `film_id` BIGINT NOT NULL,
  `content` TEXT NOT NULL,
  `rating` INT,
  `create_at` DATETIME,
  `update_at` DATETIME,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_film_id` (`film_id`),
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_comment_film` FOREIGN KEY (`film_id`) REFERENCES `films` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 订单表
CREATE TABLE IF NOT EXISTS `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_no` VARCHAR(50) NOT NULL,
  `user_id` BIGINT NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `payment_method` VARCHAR(50),
  `status` INT NOT NULL,
  `create_at` DATETIME,
  `pay_time` DATETIME,
  `update_at` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户剧集关注表
CREATE TABLE IF NOT EXISTS `user_drama_follows` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `film_id` BIGINT NOT NULL,
  `create_at` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_film` (`user_id`, `film_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_film_id` (`film_id`),
  CONSTRAINT `fk_drama_follow_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_drama_follow_film` FOREIGN KEY (`film_id`) REFERENCES `films` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户观看历史表
CREATE TABLE IF NOT EXISTS `user_watch_histories` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `film_id` BIGINT NOT NULL,
  `watch_time` DATETIME NOT NULL,
  `watch_duration` INT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_film_id` (`film_id`),
  KEY `idx_watch_time` (`watch_time`),
  CONSTRAINT `fk_watch_history_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_watch_history_film` FOREIGN KEY (`film_id`) REFERENCES `films` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 视频来源表
CREATE TABLE IF NOT EXISTS `sources` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `url` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初始化系统角色
INSERT INTO `roles` (`name`, `description`) VALUES
('ROLE_ADMIN', '系统管理员'),
('ROLE_USER', '普通用户'),
('ROLE_VIP', 'VIP会员');

-- 初始化管理员账户
INSERT INTO `users` (`username`, `password`, `email`, `status`, `member_level`, `register_time`, `update_at`) VALUES
('admin', '$2a$10$YPFm6StJQYHMW3vdTNSmrO2vu36QVdPPZ7Kh/Yl.25V/ARw1XZjLO', 'admin@example.com', 1, 0, NOW(), NOW());

-- 分配管理员角色
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1);

-- 初始化基础分类
INSERT INTO `categories` (`name`, `description`, `create_at`, `update_at`) VALUES
('电影', '所有电影作品', NOW(), NOW()),
('电视剧', '所有电视剧作品', NOW(), NOW()),
('动漫', '所有动漫作品', NOW(), NOW()),
('综艺', '所有综艺作品', NOW(), NOW());

-- 启用外键约束检查
SET FOREIGN_KEY_CHECKS = 1; 