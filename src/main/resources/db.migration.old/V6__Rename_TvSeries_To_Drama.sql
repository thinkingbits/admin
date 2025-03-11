-- 重命名电视剧表
RENAME TABLE tv_series TO dramas;

-- 重命名季表中的外键关联
ALTER TABLE seasons DROP FOREIGN KEY seasons_ibfk_1;
ALTER TABLE seasons CHANGE tv_series_id drama_id BIGINT;
ALTER TABLE seasons ADD CONSTRAINT seasons_ibfk_1 FOREIGN KEY (drama_id) REFERENCES dramas(id);

-- 重命名演员关联表
RENAME TABLE tv_series_actors TO drama_actors;
ALTER TABLE drama_actors DROP FOREIGN KEY tv_series_actors_ibfk_1;
ALTER TABLE drama_actors CHANGE tv_series_id drama_id BIGINT;
ALTER TABLE drama_actors ADD CONSTRAINT drama_actors_ibfk_1 FOREIGN KEY (drama_id) REFERENCES dramas(id);

-- 重命名用户观看历史表中的外键关联
ALTER TABLE user_watch_history DROP FOREIGN KEY user_watch_history_ibfk_2;
ALTER TABLE user_watch_history CHANGE tv_series_id drama_id BIGINT;
ALTER TABLE user_watch_history ADD CONSTRAINT user_watch_history_ibfk_2 FOREIGN KEY (drama_id) REFERENCES dramas(id);

-- 重命名用户追剧表
RENAME TABLE user_tv_follows TO user_drama_follows;
ALTER TABLE user_drama_follows DROP FOREIGN KEY user_tv_follows_ibfk_2;
ALTER TABLE user_drama_follows CHANGE tv_series_id drama_id BIGINT;
ALTER TABLE user_drama_follows ADD CONSTRAINT user_drama_follows_ibfk_2 FOREIGN KEY (drama_id) REFERENCES dramas(id); 