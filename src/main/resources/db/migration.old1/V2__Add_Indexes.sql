-- 数据库索引优化脚本 V2
-- 添加额外的索引以提高查询性能

-- 电影相关索引
CREATE INDEX idx_film_title_rating ON films (title, rating);
CREATE INDEX idx_film_region ON films (region);
CREATE INDEX idx_film_language ON films (language);
CREATE INDEX idx_film_type ON films (type);
CREATE INDEX idx_film_create_at ON films (create_at);

-- 演员相关索引
CREATE INDEX idx_actor_original_name ON actors (original_name);
CREATE INDEX idx_actor_create_at ON actors (create_at);

-- 导演相关索引
CREATE INDEX idx_director_original_name ON directors (original_name);
CREATE INDEX idx_director_create_at ON directors (create_at);

-- 用户相关索引
CREATE INDEX idx_user_status ON users (status);
CREATE INDEX idx_user_member_level ON users (member_level);
CREATE INDEX idx_user_register_time ON users (register_time);

-- 评论相关索引
CREATE INDEX idx_comment_rating ON comments (rating);
CREATE INDEX idx_comment_create_at ON comments (create_at);

-- 播放历史相关索引
CREATE INDEX idx_play_history_watch_status ON play_histories (watch_status);
CREATE INDEX idx_play_history_last_play_time ON play_histories (last_play_time);

-- 订单相关索引
CREATE INDEX idx_order_status ON orders (status);
CREATE INDEX idx_order_create_at ON orders (create_at);
CREATE INDEX idx_order_pay_time ON orders (pay_time);

-- 用户观看历史相关索引
CREATE INDEX idx_user_watch_history_duration ON user_watch_histories (watch_duration); 