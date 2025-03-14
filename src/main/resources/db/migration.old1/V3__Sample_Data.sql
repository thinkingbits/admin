-- 样本数据插入脚本 V3
-- 添加示例电影、导演、演员等数据

-- 禁用外键约束检查
SET FOREIGN_KEY_CHECKS = 0;

-- 插入演员数据
INSERT INTO `actors` (`name`, `original_name`, `avatar_url`, `description`, `create_at`, `update_at`) VALUES
('汤姆·汉克斯', 'Tom Hanks', 'https://example.com/avatars/tom_hanks.jpg', '托马斯·杰弗里·"汤姆"·汉克斯（Thomas Jeffrey "Tom" Hanks，1956年7月9日－），美国演员、电影导演及编剧。', NOW(), NOW()),
('罗伯特·唐尼', 'Robert Downey Jr.', 'https://example.com/avatars/robert_downey.jpg', '小罗伯特·约翰·唐尼（Robert John Downey Jr.，1965年4月4日－），是一名美国男演员。', NOW(), NOW()),
('斯嘉丽·约翰逊', 'Scarlett Johansson', 'https://example.com/avatars/scarlett_johansson.jpg', '斯嘉丽·约翰逊（Scarlett Johansson，1984年11月22日－）是一名美国女演员、歌手。', NOW(), NOW()),
('克里斯·埃文斯', 'Chris Evans', 'https://example.com/avatars/chris_evans.jpg', '克里斯托弗·罗伯特·埃文斯（Christopher Robert Evans，1981年6月13日－），美国演员。', NOW(), NOW()),
('莱昂纳多·迪卡普里奥', 'Leonardo DiCaprio', 'https://example.com/avatars/leonardo_dicaprio.jpg', '莱昂纳多·威廉姆·迪卡普里奥（Leonardo Wilhelm DiCaprio，1974年11月11日－），美国演员、电影制片人、环保主义者。', NOW(), NOW()),
('娜塔莉·波特曼', 'Natalie Portman', 'https://example.com/avatars/natalie_portman.jpg', '娜塔莉·波特曼（Natalie Portman，1981年6月9日－），美国演员，出生于以色列耶路撒冷。', NOW(), NOW()),
('成龙', 'Jackie Chan', 'https://example.com/avatars/jackie_chan.jpg', '成龙（Jackie Chan），1954年4月7日出生于香港，中国香港演员、导演、动作指导、制作人、编剧。', NOW(), NOW()),
('杨紫琼', 'Michelle Yeoh', 'https://example.com/avatars/michelle_yeoh.jpg', '杨紫琼（Michelle Yeoh，1962年8月6日－），马来西亚华裔女演员。', NOW(), NOW()),
('宋仲基', 'Song Joong-ki', 'https://example.com/avatars/song_joong_ki.jpg', '宋仲基（韩语：송중기，1985年9月19日－），韩国男演员。', NOW(), NOW()),
('新垣结衣', 'Aragaki Yui', 'https://example.com/avatars/aragaki_yui.jpg', '新垣结衣（1988年6月11日－），日本女演员、歌手及模特儿，出生于冲绳县那霸市。', NOW(), NOW());

-- 插入导演数据
INSERT INTO `directors` (`name`, `original_name`, `avatar_url`, `description`, `create_at`, `update_at`) VALUES
('克里斯托弗·诺兰', 'Christopher Nolan', 'https://example.com/avatars/christopher_nolan.jpg', '克里斯托弗·爱德华·诺兰（Christopher Edward Nolan，1970年7月30日－），英美双重国籍电影导演、编剧及制作人。', NOW(), NOW()),
('史蒂文·斯皮尔伯格', 'Steven Spielberg', 'https://example.com/avatars/steven_spielberg.jpg', '史蒂文·艾伦·斯皮尔伯格（Steven Allan Spielberg，1946年12月18日－），美国导演、编剧及电影制片人。', NOW(), NOW()),
('詹姆斯·卡梅隆', 'James Cameron', 'https://example.com/avatars/james_cameron.jpg', '詹姆斯·弗朗西斯·卡梅隆（James Francis Cameron，1954年8月16日－），加拿大电影导演、编剧、制片人。', NOW(), NOW()),
('李安', 'Ang Lee', 'https://example.com/avatars/ang_lee.jpg', '李安（Ang Lee，1954年10月23日－），台湾男导演、编剧及监制。', NOW(), NOW()),
('昆汀·塔伦蒂诺', 'Quentin Tarantino', 'https://example.com/avatars/quentin_tarantino.jpg', '昆汀·杰罗姆·塔伦蒂诺（Quentin Jerome Tarantino，1963年3月27日－），美国导演及编剧。', NOW(), NOW()),
('宫崎骏', 'Hayao Miyazaki', 'https://example.com/avatars/hayao_miyazaki.jpg', '宫崎骏（1941年1月5日－），日本动画师、动画导演、漫画家及吉卜力工作室的创办人。', NOW(), NOW()),
('王家卫', 'Wong Kar-wai', 'https://example.com/avatars/wong_kar_wai.jpg', '王家卫（1958年7月17日－），香港电影导演、编剧及制片人。', NOW(), NOW()),
('奉俊昊', 'Bong Joon-ho', 'https://example.com/avatars/bong_joon_ho.jpg', '奉俊昊（韩语：봉준호，1969年9月14日－），韩国导演及编剧。', NOW(), NOW());

-- 插入电影数据
INSERT INTO `films` (`type`, `title`, `original_title`, `rating`, `region`, `language`, `description`, `category_id`, `poster_url`, `banner_url`, `video_url`, `air_date`, `status`, `create_at`, `update_at`) VALUES
(1, '盗梦空间', 'Inception', 9.3, '美国', '英语', '道姆·科布（莱昂纳多·迪卡普里奥饰）是一名经验老到的窃贼，他在梦境共享技术领域中找到了一个特殊的位置，那就是在目标人物的梦中窃取有价值的秘密。', 1, 'https://example.com/posters/inception.jpg', 'https://example.com/banners/inception.jpg', 'https://example.com/videos/inception.mp4', '2010-07-16', 1, NOW(), NOW()),
(1, '阿甘正传', 'Forrest Gump', 9.5, '美国', '英语', '阿甘（汤姆·汉克斯饰）于二战后不久出生在美国南方阿拉巴马州一个闭塞的小镇，他先天弱智，智商只有75，然而他的妈妈是一个性格坚强的女性，她常常鼓励阿甘"傻人有傻福"。', 1, 'https://example.com/posters/forrest_gump.jpg', 'https://example.com/banners/forrest_gump.jpg', 'https://example.com/videos/forrest_gump.mp4', '1994-07-06', 1, NOW(), NOW()),
(1, '复仇者联盟', 'The Avengers', 8.1, '美国', '英语', '当一股突如其来的强大邪恶势力威胁到全球安全时，神盾局指挥官尼克·弗瑞尝试组建一个拯救世界的团队，于是他创建了"复仇者联盟"计划。', 1, 'https://example.com/posters/avengers.jpg', 'https://example.com/banners/avengers.jpg', 'https://example.com/videos/avengers.mp4', '2012-05-04', 1, NOW(), NOW()),
(1, '泰坦尼克号', 'Titanic', 9.4, '美国', '英语', '1912年4月10日，号称"世界工业史上的奇迹"的豪华客轮泰坦尼克号开始了自己的处女航，从英国的南安普顿出发驶往美国纽约。', 1, 'https://example.com/posters/titanic.jpg', 'https://example.com/banners/titanic.jpg', 'https://example.com/videos/titanic.mp4', '1997-12-19', 1, NOW(), NOW()),
(1, '寄生虫', 'Parasite', 8.8, '韩国', '韩语', '基宇（崔宇植饰）一家四口居住在一间阴暗潮湿的半地下室中，生活艰辛，却依然乐观。一天，基宇的好友送他一块"招财石"，并介绍他去一个有钱人家里做高中生英语家教。', 1, 'https://example.com/posters/parasite.jpg', 'https://example.com/banners/parasite.jpg', 'https://example.com/videos/parasite.mp4', '2019-05-30', 1, NOW(), NOW()),
(1, '千与千寻', 'Spirited Away', 9.4, '日本', '日语', '10岁的少女千寻与父母一起从都市搬家到了乡下。没想到在搬家的途中，一家人发现了一条神秘隧道，走出隧道眼前竟是一片荒无人烟的广场。', 3, 'https://example.com/posters/spirited_away.jpg', 'https://example.com/banners/spirited_away.jpg', 'https://example.com/videos/spirited_away.mp4', '2001-07-20', 1, NOW(), NOW()),
(2, '请回答1988', 'Reply 1988', 9.7, '韩国', '韩语', '《请回答1988》讲述了1988年汉城奥运会前后，住在首尔市道峰区双门洞的五家人的故事：善宇家、正焕家、善宇家、东龙家和泰俊家。', 2, 'https://example.com/posters/reply_1988.jpg', 'https://example.com/banners/reply_1988.jpg', 'https://example.com/videos/reply_1988.mp4', '2015-11-06', 1, NOW(), NOW()),
(2, '大明王朝1566', 'Ming Dynasty 1566', 9.7, '中国', '汉语', '该剧主要讲述了嘉靖皇帝与内阁首辅徐阶开启明朝最后一次新政，却最终随着严嵩的崛起而失败的故事。', 2, 'https://example.com/posters/ming_dynasty.jpg', 'https://example.com/banners/ming_dynasty.jpg', 'https://example.com/videos/ming_dynasty.mp4', '2007-01-01', 1, NOW(), NOW()),
(2, '神探夏洛克', 'Sherlock', 9.5, '英国', '英语', '《神探夏洛克》是一部由英国广播公司（BBC）拍摄的电视剧，改编自阿瑟·柯南·道尔的侦探小说《夏洛克·福尔摩斯》。', 2, 'https://example.com/posters/sherlock.jpg', 'https://example.com/banners/sherlock.jpg', 'https://example.com/videos/sherlock.mp4', '2010-07-25', 1, NOW(), NOW()),
(3, '鬼灭之刃', 'Demon Slayer: Kimetsu no Yaiba', 8.9, '日本', '日语', '时值日本大正时期，少年竈门炭治郎的家人惨遭鬼杀害，而唯一幸存的妹妹禰豆子却变成了鬼。为了寻求让妹妹变回人类的方法，炭治郎决心加入"鬼杀队"。', 3, 'https://example.com/posters/demon_slayer.jpg', 'https://example.com/banners/demon_slayer.jpg', 'https://example.com/videos/demon_slayer.mp4', '2019-04-06', 1, NOW(), NOW());

-- 关联电影与导演
INSERT INTO `film_directors` (`film_id`, `director_id`) VALUES
(1, 1), -- 盗梦空间 - 克里斯托弗·诺兰
(2, 2), -- 阿甘正传 - 史蒂文·斯皮尔伯格
(3, 3), -- 复仇者联盟 - 詹姆斯·卡梅隆
(4, 3), -- 泰坦尼克号 - 詹姆斯·卡梅隆
(5, 8), -- 寄生虫 - 奉俊昊
(6, 6), -- 千与千寻 - 宫崎骏
(10, 6); -- 鬼灭之刃 - 宫崎骏

-- 关联电影与演员
INSERT INTO `film_actors` (`film_id`, `actor_id`) VALUES
(1, 5), -- 盗梦空间 - 莱昂纳多·迪卡普里奥
(2, 1), -- 阿甘正传 - 汤姆·汉克斯
(3, 2), -- 复仇者联盟 - 罗伯特·唐尼
(3, 3), -- 复仇者联盟 - 斯嘉丽·约翰逊
(3, 4), -- 复仇者联盟 - 克里斯·埃文斯
(4, 5), -- 泰坦尼克号 - 莱昂纳多·迪卡普里奥
(7, 9), -- 请回答1988 - 宋仲基
(10, 10); -- 鬼灭之刃 - 新垣结衣

-- 添加季数据
INSERT INTO `seasons` (`title`, `film_id`, `season_number`, `create_at`, `update_at`) VALUES
('请回答1988 第一季', 7, 1, NOW(), NOW()),
('请回答1988 第二季', 7, 2, NOW(), NOW()),
('神探夏洛克 第一季', 9, 1, NOW(), NOW()),
('神探夏洛克 第二季', 9, 2, NOW(), NOW()),
('神探夏洛克 第三季', 9, 3, NOW(), NOW()),
('神探夏洛克 第四季', 9, 4, NOW(), NOW()),
('鬼灭之刃 第一季', 10, 1, NOW(), NOW()),
('鬼灭之刃 第二季', 10, 2, NOW(), NOW());

-- 添加剧集数据
INSERT INTO `episodes` (`title`, `season_id`, `film_id`, `episode_number`, `video_url`, `create_at`, `update_at`) VALUES
('请回答1988 S01E01', 1, 7, 1, 'https://example.com/videos/reply_1988_s01e01.mp4', NOW(), NOW()),
('请回答1988 S01E02', 1, 7, 2, 'https://example.com/videos/reply_1988_s01e02.mp4', NOW(), NOW()),
('请回答1988 S01E03', 1, 7, 3, 'https://example.com/videos/reply_1988_s01e03.mp4', NOW(), NOW()),
('请回答1988 S02E01', 2, 7, 1, 'https://example.com/videos/reply_1988_s02e01.mp4', NOW(), NOW()),
('请回答1988 S02E02', 2, 7, 2, 'https://example.com/videos/reply_1988_s02e02.mp4', NOW(), NOW()),
('神探夏洛克 S01E01 粉色的研究', 3, 9, 1, 'https://example.com/videos/sherlock_s01e01.mp4', NOW(), NOW()),
('神探夏洛克 S01E02 盲眼银行家', 3, 9, 2, 'https://example.com/videos/sherlock_s01e02.mp4', NOW(), NOW()),
('神探夏洛克 S01E03 大游戏', 3, 9, 3, 'https://example.com/videos/sherlock_s01e03.mp4', NOW(), NOW()),
('鬼灭之刃 S01E01 残酷', 7, 10, 1, 'https://example.com/videos/demon_slayer_s01e01.mp4', NOW(), NOW()),
('鬼灭之刃 S01E02 培训', 7, 10, 2, 'https://example.com/videos/demon_slayer_s01e02.mp4', NOW(), NOW()),
('鬼灭之刃 S01E03 守护者', 7, 10, 3, 'https://example.com/videos/demon_slayer_s01e03.mp4', NOW(), NOW()),
('鬼灭之刃 S02E01 游郭篇', 8, 10, 1, 'https://example.com/videos/demon_slayer_s02e01.mp4', NOW(), NOW()),
('鬼灭之刃 S02E02 上弦之陆', 8, 10, 2, 'https://example.com/videos/demon_slayer_s02e02.mp4', NOW(), NOW());

-- 添加评论数据
INSERT INTO `comments` (`user_id`, `film_id`, `content`, `rating`, `create_at`, `update_at`) VALUES
(1, 1, '这是一部关于梦境的精彩电影，克里斯托弗·诺兰的杰作！', 5, NOW(), NOW()),
(1, 2, '汤姆·汉克斯的表演让人难以忘怀，真正的经典之作。', 5, NOW(), NOW()),
(1, 3, '漫威电影宇宙最经典的作品之一，超级英雄集结画面令人震撼。', 4, NOW(), NOW()),
(1, 6, '宫崎骏的动画作品总是充满魔力，这部尤其出色。', 5, NOW(), NOW()),
(1, 7, '这部韩剧充满了温情，让人回忆起自己的青春时光。', 5, NOW(), NOW());

-- 添加播放历史
INSERT INTO `play_histories` (`user_id`, `episode_id`, `play_position`, `duration`, `watch_status`, `last_play_time`, `create_at`, `update_at`) VALUES
(1, 1, 1800, 3600, 1, NOW(), NOW(), NOW()),
(1, 2, 900, 3600, 0, NOW(), NOW(), NOW()),
(1, 6, 2700, 5400, 1, NOW(), NOW(), NOW()),
(1, 9, 1200, 1440, 1, NOW(), NOW(), NOW());

-- 添加用户收藏
INSERT INTO `favorites` (`user_id`, `film_id`, `create_at`) VALUES
(1, 1, NOW()),
(1, 2, NOW()),
(1, 6, NOW()),
(1, 7, NOW());

-- 启用外键约束检查
SET FOREIGN_KEY_CHECKS = 1; 