-- 动漫表
CREATE TABLE animes (
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
    rating DOUBLE DEFAULT 0,
    rating_count INT DEFAULT 0,
    category_id BIGINT,
    region VARCHAR(50),
    language VARCHAR(50),
    type VARCHAR(20),
    studio VARCHAR(100),
    season VARCHAR(50),
    broadcast VARCHAR(50),
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 动漫剧集表
CREATE TABLE anime_episodes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    anime_id BIGINT,
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
    FOREIGN KEY (anime_id) REFERENCES animes(id)
); 