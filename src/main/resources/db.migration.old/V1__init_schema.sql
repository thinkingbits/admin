-- 角色表
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- 用户表
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    avatar VARCHAR(255),
    status INT NOT NULL DEFAULT 1,
    member_level INT DEFAULT 0,
    register_time DATETIME,
    last_login_time DATETIME,
    update_time DATETIME
);

-- 用户角色关联表
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 分类表
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    parent_id BIGINT,
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- 导演表
CREATE TABLE directors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    biography TEXT,
    photo_url VARCHAR(255),
    create_time DATETIME,
    update_time DATETIME
);

-- 电影表
CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    release_date DATE,
    duration INT,
    poster_url VARCHAR(255),
    video_url VARCHAR(255),
    rating DOUBLE DEFAULT 0.0,
    category_id BIGINT,
    status INT NOT NULL DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- 收藏表
CREATE TABLE favorites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    status INT DEFAULT 1,  -- 添加 status 列，默认值为 1
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    UNIQUE KEY `uk_user_movie` (user_id, movie_id)
);

-- 播放历史表
CREATE TABLE play_histories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    play_position INT DEFAULT 0,  -- 播放位置（秒）
    duration INT DEFAULT 0,       -- 视频总时长（秒）
    watch_status INT DEFAULT 0,   -- 观看状态：0-未完成 1-已完成
    last_play_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- 最后播放时间
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,     -- 首次播放时间
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 更新时间
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    UNIQUE KEY `uk_user_movie` (user_id, movie_id)
);


-- 电影导演关联表
CREATE TABLE movie_directors (
    movie_id BIGINT NOT NULL,
    director_id BIGINT NOT NULL,
    PRIMARY KEY (movie_id, director_id),
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES directors(id) ON DELETE CASCADE
);

-- 评论表
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    rating INT,
    status INT DEFAULT 0,
    create_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- 订单表
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status INT DEFAULT 0,
    payment_method VARCHAR(50),
    membership_type VARCHAR(50),
    create_time DATETIME,
    pay_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 初始化角色数据
INSERT INTO roles (name, description) VALUES 
('ADMIN', '系统管理员'),
('USER', '普通用户'),
('VIP', 'VIP会员');

-- 初始化管理员账号（密码: admin123，已使用BCrypt加密）
INSERT INTO users (username, password, email, status, member_level, register_time) VALUES 
('admin', '$2a$10$mLK.rrdlvx9DCFb6Eck1t.TlltnGulepXnov3bBp5T2TloO1MYj52', 'admin@example.com', 1, 0, NOW());

-- 初始化管理员角色
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

-- 初始化电影分类
INSERT INTO categories (name, description, create_time, update_time) VALUES 
('动作片', '刺激的动作场景', NOW(), NOW()),
('喜剧片', '轻松幽默', NOW(), NOW()),
('爱情片', '浪漫情感', NOW(), NOW()),
('科幻片', '未来科技', NOW(), NOW()),
('恐怖片', '惊悚刺激', NOW(), NOW()),
('动画片', '适合全家观看', NOW(), NOW());

-- 初始化导演数据
INSERT INTO directors (name, biography, photo_url, create_time, update_time) VALUES 
('克里斯托弗·诺兰', '英国电影导演，以《盗梦空间》、《星际穿越》等作品闻名', 'https://example.com/nolan.jpg', NOW(), NOW()),
('詹姆斯·卡梅隆', '美国电影导演，代表作《泰坦尼克号》、《阿凡达》', 'https://example.com/cameron.jpg', NOW(), NOW()),
('宫崎骏', '日本动画电影导演，吉卜力工作室创始人', 'https://example.com/miyazaki.jpg', NOW(), NOW()),
('李安', '华裔导演，曾获奥斯卡最佳导演奖', 'https://example.com/ang-lee.jpg', NOW(), NOW()),
('昆汀·塔伦蒂诺', '美国电影导演，以独特的叙事风格著称', 'https://example.com/tarantino.jpg', NOW(), NOW()),
('斯皮尔伯格', '美国著名导演，科幻片大师', 'https://example.com/spielberg.jpg', NOW(), NOW());

-- 初始化电影数据
INSERT INTO movies (title, description, release_date, duration, poster_url, video_url, rating, category_id, status, create_time, update_time) VALUES 
('盗梦空间', '一名技术高超的窃贼能够潜入他人的梦境中窃取情报', '2010-07-16', 148, 'https://example.com/inception.jpg', 'https://example.com/inception.mp4', 9.3, 4, 1, NOW(), NOW()),
('阿凡达', '一名前海军陆战队员来到潘多拉星球，卷入了一场殖民者与原住民的冲突', '2009-12-18', 162, 'https://example.com/avatar.jpg', 'https://example.com/avatar.mp4', 9.1, 4, 1, NOW(), NOW()),
('千与千寻', '小女孩千寻意外进入神灵世界，经历了一段奇妙的冒险', '2001-07-20', 125, 'https://example.com/spirited-away.jpg', 'https://example.com/spirited-away.mp4', 9.4, 6, 1, NOW(), NOW()),
('卧虎藏龙', '武侠世界中的爱情与信念', '2000-07-07', 120, 'https://example.com/crouching-tiger.jpg', 'https://example.com/crouching-tiger.mp4', 8.9, 1, 1, NOW(), NOW()),
('低俗小说', '多个独立故事交织在一起的黑色幽默犯罪片', '1994-10-14', 154, 'https://example.com/pulp-fiction.jpg', 'https://example.com/pulp-fiction.mp4', 9.2, 1, 1, NOW(), NOW()),
('侏罗纪公园', '一座充满恐龙的主题公园发生意外', '1993-06-11', 127, 'https://example.com/jurassic-park.jpg', 'https://example.com/jurassic-park.mp4', 8.8, 4, 1, NOW(), NOW());

-- 电影与导演关联数据
INSERT INTO movie_directors (movie_id, director_id) VALUES 
(1, 1), -- 盗梦空间 - 诺兰
(2, 2), -- 阿凡达 - 卡梅隆
(3, 3), -- 千与千寻 - 宫崎骏
(4, 4), -- 卧虎藏龙 - 李安
(5, 5), -- 低俗小说 - 塔伦蒂诺
(6, 6); -- 侏罗纪公园 - 斯皮尔伯格 