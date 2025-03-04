-- 电视剧表
CREATE TABLE tv_series (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    original_title VARCHAR(255),
    description TEXT,
    poster_url VARCHAR(255),
    banner_url VARCHAR(255),
    release_date DATE,
    end_date DATE,
    total_episodes INT,
    current_episodes INT,
    rating DECIMAL(2,1) DEFAULT 0,
    rating_count INT DEFAULT 0,
    category_id BIGINT,
    region VARCHAR(50),
    language VARCHAR(50),
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 季表
CREATE TABLE seasons (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tv_series_id BIGINT,
    title VARCHAR(255),
    description TEXT,
    season_number INT,
    episode_count INT,
    release_date DATE,
    end_date DATE,
    poster_url VARCHAR(255),
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (tv_series_id) REFERENCES tv_series(id)
);

-- 剧集表
CREATE TABLE episodes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    season_id BIGINT,
    title VARCHAR(255),
    episode_number INT,
    duration INT,
    video_url VARCHAR(255),
    thumbnail_url VARCHAR(255),
    description TEXT,
    air_date DATE,
    view_count BIGINT DEFAULT 0,
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (season_id) REFERENCES seasons(id)
);

-- 演员表
CREATE TABLE actors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    original_name VARCHAR(100),
    avatar_url VARCHAR(255),
    description TEXT,
    create_time DATETIME,
    update_time DATETIME
);

-- 电视剧-演员关联表
CREATE TABLE tv_series_actors (
    tv_series_id BIGINT,
    actor_id BIGINT,
    role_name VARCHAR(100),
    role_type INT,
    create_time DATETIME,
    PRIMARY KEY (tv_series_id, actor_id),
    FOREIGN KEY (tv_series_id) REFERENCES tv_series(id),
    FOREIGN KEY (actor_id) REFERENCES actors(id)
);

-- 用户观看历史表
CREATE TABLE user_watch_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    tv_series_id BIGINT,
    season_id BIGINT,
    episode_id BIGINT,
    watch_progress INT DEFAULT 0,
    last_watch_time DATETIME,
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (tv_series_id) REFERENCES tv_series(id),
    FOREIGN KEY (season_id) REFERENCES seasons(id),
    FOREIGN KEY (episode_id) REFERENCES episodes(id)
);

-- 用户追剧表
CREATE TABLE user_tv_follows (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    tv_series_id BIGINT,
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (tv_series_id) REFERENCES tv_series(id)
); 