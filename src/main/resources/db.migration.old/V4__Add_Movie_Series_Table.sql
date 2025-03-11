-- 电影系列表
CREATE TABLE movie_series (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    original_title VARCHAR(255),
    description TEXT,
    poster_url VARCHAR(255),
    banner_url VARCHAR(255),
    start_date DATE,
    end_date DATE,
    total_movies INT,
    rating DOUBLE DEFAULT 0,
    rating_count INT DEFAULT 0,
    category_id BIGINT,
    region VARCHAR(50),
    language VARCHAR(50),
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 修改电影表，添加系列相关字段
ALTER TABLE movies 
ADD COLUMN movie_series_id BIGINT,
ADD COLUMN series_order INT,
ADD FOREIGN KEY (movie_series_id) REFERENCES movie_series(id); 