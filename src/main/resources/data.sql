insert into user(user_Id, user_password, email, nickname, memo, birthday, created_at)
values ('user', '{noop}password', 'email@email.com', 'nick', 'memo', now(), '2023-03-13T00:00:00');

insert into vote(category, content, create_at, display_range, expired_at, is_multi_pick, modified_at, title, user_id)
values ('WORRY', 'This is #content', '2023-03-14T00:00:00', 'FRIEND', now(), false, '2023-03-14T00:00:00', 'title',
        'user');

insert into vote_option(content, image_link, vote_id)
values ('new option1', '/link/image1.png', 1);

insert into vote_option(content, image_link, vote_id)
values ('new option2', '/link/image2.png', 1);

insert into hashtag(content)
values ('content');

insert into vote_hashtag(hashtag_id, vote_id)
values (1, 1);

insert into user(user_Id, user_password, email, nickname, memo, birthday, created_at)
values ('user2', '{noop}password', 'email@email.com', 'nick2', 'memo', now(), '2023-03-13T00:00:00');

insert into user(user_Id, user_password, email, nickname, memo, birthday, created_at)
values ('user3', '{noop}password', 'email@email.com', 'nick3', 'memo', now(), '2023-03-13T00:00:00');

insert into pick(user_id, vote_option_id)
values ('user', 1);

insert into pick(user_id, vote_option_id)
values ('user2', 1);

insert into vote(category, content, create_at, display_range, expired_at, is_multi_pick, modified_at, title, user_id)
values ('FREE', 'This is #freecontent2', '2023-03-14T01:00:00', 'FRIEND', now(), false, '2023-03-14T01:00:00', 'FREE',
        'user2');

insert into hashtag(content)
values ('freecontent2');

insert into vote_hashtag(hashtag_id, vote_id)
values (2, 2);

insert into vote(category, content, create_at, display_range, expired_at, is_multi_pick, modified_at, title, user_id)
values ('SURVEY', 'This is SURVEY. this #is #hashtag', '2023-03-14T02:00:00', 'FRIEND', now(), false,
        '2023-03-14T02:00:00', 'SURVEY', 'user2');

insert into hashtag(content)
values ('is');

insert into hashtag(content)
values ('hashtag');

insert into vote_hashtag(hashtag_id, vote_id)
values (3, 3);

insert into vote_hashtag(hashtag_id, vote_id)
values (4, 3);

insert into vote(category, content, create_at, display_range, expired_at, is_multi_pick, modified_at, title, user_id)
values ('ENTERPRISE', '#This is ENTERPRISE', '2023-03-14T03:00:00', 'FRIEND', now(), false, '2023-03-14T03:00:00',
        'ENTERPRISE', 'user2');

insert into hashtag(content)
values ('This');

insert into vote_hashtag(hashtag_id, vote_id)
values (5, 4);

insert into vote_option(content, image_link, vote_id)
values ('new option1', '/link/image1.png', 4);

insert into vote_option(content, image_link, vote_id)
values ('new option2', '/link/image2.png', 4);

insert into pick(user_id, vote_option_id)
values ('user2', 3);

insert into vote(category, content, create_at, display_range, expired_at, is_multi_pick, modified_at, title, user_id)
values ('WORRY', '#ThisIsWORRY', '2023-03-14T04:00:00', 'FRIEND', now(), false, '2023-03-14T04:00:00', 'WORRY',
        'user2');

insert into hashtag(content)
values ('ThisIsWORRY');

insert into vote_hashtag(hashtag_id, vote_id)
values (6, 5);

insert into vote_comment(vote_id, user_id, content, create_at, modified_at)
values (1, 'user', '재밌다', now(), now());

insert into vote_comment(vote_id, user_id, content, create_at, modified_at)
values (1, 'user2', '재밌냐?', now(), now());

insert into vote_comment(vote_id, user_id, content, create_at, modified_at)
values (1, 'user3', '재미없어', now(), now());

insert into comment_like(vote_comment_id, user_id)
values (1, 'user2');

insert into comment_like(vote_comment_id, user_id)
values (2, 'user3');



-- 5 users
insert into user (user_id, user_password, email, nickname, memo, birthday, created_at) values ('cvaldes0', '11tMPQ4', 'bscrivenor0@bluehost.com', 'Skiles and Sons', 'Optimized tangible customer loyalty', '2021-07-31 05:54:08', '2022-05-13 02:43:28');
insert into user (user_id, user_password, email, nickname, memo, birthday, created_at) values ('lness1', 'aA7tIdKQAL5', 'vkilcullen1@etsy.com', 'Nikolaus-Feest', 'Enhanced secondary website', '2020-10-18 06:22:01', '2021-06-08 06:05:49');
insert into user (user_id, user_password, email, nickname, memo, birthday, created_at) values ('hcampa2', 'UCaygi', 'lretchless2@wordpress.org', 'Bins, Baumbach and Russel', 'Multi-tiered interactive access', '2021-08-06 22:23:09', '2022-02-15 18:57:26');
insert into user (user_id, user_password, email, nickname, memo, birthday, created_at) values ('tjackalin3', 'gLmKrEwLUO', 'chedling3@i2i.jp', 'Hand-Stamm', 'Adaptive intermediate task-force', '2022-10-08 06:43:59', '2022-02-02 11:37:00');
insert into user (user_id, user_password, email, nickname, memo, birthday, created_at) values ('rclayson4', 'IVKw1XpapbzR', 'bbevis4@ehow.com', 'Strosin-O''Kon', 'Universal modular website', '2023-02-24 02:29:58', '2021-01-04 00:54:05');

-- 10 votes
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (1, 'cvaldes0', 'Monsturd', 'Integrated #intermediate functionalities', 'FREE', '2023-01-02 17:18:40', '2023-02-10 11:33:14', '2021-04-11 07:35:55', 'ALL', true);
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (2, 'cvaldes0', 'Raincoat', 'Switchable fresh-thinking #secured line', 'SURVEY', '2022-03-28 16:01:59', '2022-06-09 02:20:49', '2021-04-07 01:20:52', 'FRIEND', true);
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (3, 'lness1', 'Expelled', 'Inverse user-facing #interface', 'FREE', '2022-07-31 21:42:57', '2021-05-24 05:16:01', '2021-12-31 15:59:36', 'ALL', false);
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (4, 'lness1', 'Revengers Tragedy', 'Phased #national productivity', 'SURVEY', '2022-10-31 19:26:14', '2020-08-12 15:50:03', '2020-11-12 23:49:37', 'FRIEND', false);
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (5, 'hcampa2', 'Hair Show', 'Pre-emptive maximized #hub', 'FREE', '2022-10-30 14:39:50', '2022-09-12 13:27:59', '2023-01-08 11:09:39', 'Electrical', true);
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (6, 'hcampa2', 'God''s Comedy (A Comédia de Deus)', 'Fully-configurable high-level #data-warehouse', 'WORRY', '2022-09-11 17:28:16', '2021-02-25 20:20:31', '2021-08-03 04:47:17', 'ALL', false);
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (7, 'tjackalin3', 'Guilty #Hands', 'Profit-focused high-level #protocol', 'FREE', '2022-09-04 00:48:32', '2021-09-26 11:03:36', '2021-12-13 12:09:56', 'ALL', true);
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (8, 'tjackalin3', 'Ivan the Terrible, Part Two (Ivan Groznyy II: Boyarsky zagovor)', 'Polarised composite #interface', 'WORRY', '2023-01-12 09:24:16', '2020-10-29 15:06:22', '2020-12-26 06:09:46', 'FRIEND', true);
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (9, 'rclayson4', 'Lucky Night', '#Innovative mission-critical artificial intelligence', 'FREE', '2022-07-05 12:07:09', '2021-02-15 18:33:55', '2020-12-07 09:52:28', 'FRIEND', false);
insert into vote (vote_id, user_id, title, content, category, expired_at, create_at, modified_at, display_range, is_multi_pick) values (10, 'rclayson4', 'Scalphunters, The', 'Centralized fault-tolerant #structure', 'WORRY', '2023-03-13 11:00:24', '2021-06-19 20:38:28', '2021-04-04 15:15:26', 'ALL', false);


-- 20 vote options
insert into vote_option (vote_option_id, vote_id, content, image_link) values (1, 1, 'Expanded needs-based standardization', 'http://dummyimage.com/179x100.png/dddddd/000000');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (2, 1, 'Intuitive 3rd generation open system', 'http://dummyimage.com/237x100.png/ff4444/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (3, 2, 'Advanced user-facing system engine', 'http://dummyimage.com/149x100.png/cc0000/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (4, 2, 'Optimized dedicated model', 'http://dummyimage.com/104x100.png/dddddd/000000');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (5, 3, 'User-centric client-server website', 'http://dummyimage.com/200x100.png/5fa2dd/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (6, 3, 'Expanded bottom-line budgetary management', 'http://dummyimage.com/105x100.png/cc0000/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (7, 4, 'Re-engineered intermediate framework', 'http://dummyimage.com/210x100.png/dddddd/000000');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (8, 4, 'Digitized scalable software', 'http://dummyimage.com/167x100.png/dddddd/000000');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (9, 5, 'Self-enabling fault-tolerant firmware', 'http://dummyimage.com/121x100.png/cc0000/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (10, 5, 'Customizable mobile collaboration', 'http://dummyimage.com/245x100.png/dddddd/000000');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (11, 6, 'Realigned context-sensitive adapter', 'http://dummyimage.com/238x100.png/5fa2dd/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (12, 6, 'Inverse transitional collaboration', 'http://dummyimage.com/222x100.png/ff4444/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (13, 7, 'Sharable uniform success', 'http://dummyimage.com/170x100.png/5fa2dd/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (14, 7, 'Vision-oriented background solution', 'http://dummyimage.com/164x100.png/dddddd/000000');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (15, 8, 'Multi-channelled discrete migration', 'http://dummyimage.com/159x100.png/ff4444/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (16, 8, 'Self-enabling didactic projection', 'http://dummyimage.com/171x100.png/dddddd/000000');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (17, 9, 'Synergistic full-range strategy', 'http://dummyimage.com/207x100.png/cc0000/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (18, 9, 'Distributed homogeneous Graphic Interface', 'http://dummyimage.com/130x100.png/5fa2dd/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (19, 10, 'Customizable modular orchestration', 'http://dummyimage.com/243x100.png/cc0000/ffffff');
insert into vote_option (vote_option_id, vote_id, content, image_link) values (20, 10, 'Total explicit conglomeration', 'http://dummyimage.com/157x100.png/cc0000/ffffff');

-- 30 vote comments
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (1, 1, 'tjackalin3', 'Inverse methodical solution', '2022-01-30 13:59:17', '2022-11-18 14:10:01');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (2, 1, 'lness1', 'Cloned radical parallelism', '2023-01-31 15:50:25', '2020-10-09 01:20:27');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (3, 1, 'hcampa2', 'Multi-tiered reciprocal adapter', '2023-01-18 18:52:03', '2023-02-24 06:47:36');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (4, 2, 'cvaldes0', 'Self-enabling executive framework', '2022-05-12 07:47:26', '2021-02-13 09:25:54');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (5, 2, 'rclayson4', 'Virtual object-oriented neural-net', '2021-11-22 06:59:56', '2021-03-06 00:08:45');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (6, 2, 'hcampa2', 'Devolved 3rd generation algorithm', '2021-05-23 07:56:04', '2021-06-08 13:08:52');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (7, 3, 'cvaldes0', 'Operative responsive migration', '2022-04-12 17:58:08', '2022-10-02 07:17:33');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (8, 3, 'tjackalin3', 'Quality-focused bandwidth-monitored budgetary management', '2022-10-28 18:46:14', '2022-07-05 18:13:54');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (9, 3, 'hcampa2', 'Decentralized global functionalities', '2022-11-10 17:49:01', '2021-12-06 22:41:18');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (10, 4, 'rclayson4', 'Sharable analyzing attitude', '2022-07-26 10:36:31', '2022-11-04 11:38:47');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (11, 4, 'lness1', 'Adaptive real-time encryption', '2022-06-08 12:21:21', '2022-05-18 07:41:33');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (12, 4, 'hcampa2', 'Ergonomic multi-tasking help-desk', '2021-11-15 17:04:25', '2020-12-06 17:30:37');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (13, 5, 'cvaldes0', 'Front-line homogeneous standardization', '2022-12-27 00:47:41', '2023-02-27 06:52:55');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (14, 5, 'lness1', 'Managed intangible collaboration', '2022-10-25 12:57:38', '2022-02-20 09:06:30');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (15, 5, 'hcampa2', 'Team-oriented tertiary solution', '2021-10-08 04:58:38', '2021-11-28 00:24:54');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (16, 6, 'tjackalin3', 'Self-enabling asynchronous service-desk', '2021-07-26 18:00:40', '2022-03-28 00:32:32');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (17, 6, 'lness1', 'Enhanced encompassing frame', '2022-09-04 09:38:15', '2022-10-03 12:01:00');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (18, 6, 'rclayson4', 'Fundamental optimal migration', '2023-01-07 20:18:38', '2022-01-28 12:08:08');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (19, 7, 'cvaldes0', 'Customizable responsive concept', '2021-06-06 01:18:41', '2022-02-13 16:46:46');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (20, 7, 'lness1', 'Focused next generation definition', '2022-05-15 19:59:03', '2023-02-04 15:08:54');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (21, 7, 'hcampa2', 'Realigned solution-oriented encoding', '2023-01-23 12:26:35', '2021-12-22 00:33:39');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (22, 8, 'cvaldes0', 'Reverse-engineered executive success', '2022-01-31 15:21:11', '2022-03-27 04:07:19');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (23, 8, 'tjackalin3', 'Down-sized analyzing emulation', '2021-12-03 06:56:05', '2021-12-06 04:05:34');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (24, 8, 'rclayson4', 'Intuitive optimal groupware', '2021-11-20 12:31:52', '2021-03-14 23:10:54');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (25, 9, 'cvaldes0', 'Progressive fault-tolerant Graphic Interface', '2022-02-23 16:10:56', '2021-09-10 19:32:34');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (26, 9, 'lness1', 'Secured web-enabled capacity', '2023-03-11 22:11:08', '2021-08-25 15:55:43');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (27, 9, 'hcampa2', 'Advanced optimizing access', '2022-02-12 11:17:35', '2021-12-13 11:53:37');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (28, 10, 'cvaldes0', 'Re-contextualized asymmetric function', '2021-10-14 16:01:41', '2022-08-12 05:58:48');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (29, 10, 'lness1', 'Visionary user-facing frame', '2023-03-05 06:27:21', '2022-09-16 08:06:21');
insert into vote_comment (vote_comment_id, vote_id, user_id, content, create_at, modified_at) values (30, 10, 'rclayson4', 'Operative mobile Graphical User Interface', '2023-02-11 02:06:27', '2021-07-22 08:31:44');

-- 10 hashtags
insert into hashtag(hashtag_id, content) VALUES (1, 'intermediate');
insert into hashtag(hashtag_id, content) VALUES (2, 'secured');
insert into hashtag(hashtag_id, content) VALUES (3, 'interface');
insert into hashtag(hashtag_id, content) VALUES (4, 'national');
insert into hashtag(hashtag_id, content) VALUES (5, 'hub');
insert into hashtag(hashtag_id, content) VALUES (6, 'data-warehouse');
insert into hashtag(hashtag_id, content) VALUES (7, 'protocol');
insert into hashtag(hashtag_id, content) VALUES (8, 'interface');
insert into hashtag(hashtag_id, content) VALUES (9, 'Innovative');
insert into hashtag(hashtag_id, content) VALUES (10, 'structure');

-- 10 vote_hashtags
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (1, 1, 1);
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (2, 2, 2);
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (3, 3, 3);
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (4, 4, 4);
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (5, 5, 5);
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (6, 6, 6);
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (7, 7, 7);
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (8, 8, 8);
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (9, 9, 9);
insert into vote_hashtag(id, hashtag_id, vote_id) VALUES (10, 10, 10);


-- 40 picks
insert into pick(pick_id, user_id, vote_option_id) VALUES (1, 1, 1);
insert into pick(pick_id, user_id, vote_option_id) VALUES (2, 1, 3);
insert into pick(pick_id, user_id, vote_option_id) VALUES (3, 1, 5);
insert into pick(pick_id, user_id, vote_option_id) VALUES (4, 1, 9);
insert into pick(pick_id, user_id, vote_option_id) VALUES (5, 1, 11);
insert into pick(pick_id, user_id, vote_option_id) VALUES (6, 1, 13);
insert into pick(pick_id, user_id, vote_option_id) VALUES (7, 1, 15);
insert into pick(pick_id, user_id, vote_option_id) VALUES (8, 1, 17);
insert into pick(pick_id, user_id, vote_option_id) VALUES (9, 1, 20);
insert into pick(pick_id, user_id, vote_option_id) VALUES (10, 2, 1);
insert into pick(pick_id, user_id, vote_option_id) VALUES (11, 2, 4);
insert into pick(pick_id, user_id, vote_option_id) VALUES (12, 2, 5);
insert into pick(pick_id, user_id, vote_option_id) VALUES (13, 2, 11);
insert into pick(pick_id, user_id, vote_option_id) VALUES (14, 2, 13);
insert into pick(pick_id, user_id, vote_option_id) VALUES (15, 2, 17);
insert into pick(pick_id, user_id, vote_option_id) VALUES (16, 2, 20);
insert into pick(pick_id, user_id, vote_option_id) VALUES (17, 3, 2);
insert into pick(pick_id, user_id, vote_option_id) VALUES (18, 3, 4);
insert into pick(pick_id, user_id, vote_option_id) VALUES (19, 3, 6);
insert into pick(pick_id, user_id, vote_option_id) VALUES (20, 3, 8);
insert into pick(pick_id, user_id, vote_option_id) VALUES (21, 3, 10);
insert into pick(pick_id, user_id, vote_option_id) VALUES (22, 3, 15);
insert into pick(pick_id, user_id, vote_option_id) VALUES (23, 3, 19);
insert into pick(pick_id, user_id, vote_option_id) VALUES (24, 4, 1);
insert into pick(pick_id, user_id, vote_option_id) VALUES (25, 4, 3);
insert into pick(pick_id, user_id, vote_option_id) VALUES (26, 4, 5);
insert into pick(pick_id, user_id, vote_option_id) VALUES (27, 4, 7);
insert into pick(pick_id, user_id, vote_option_id) VALUES (28, 4, 9);
insert into pick(pick_id, user_id, vote_option_id) VALUES (29, 4, 11);
insert into pick(pick_id, user_id, vote_option_id) VALUES (30, 4, 13);
insert into pick(pick_id, user_id, vote_option_id) VALUES (31, 4, 17);
insert into pick(pick_id, user_id, vote_option_id) VALUES (32, 4, 19);
insert into pick(pick_id, user_id, vote_option_id) VALUES (33, 5, 2);
insert into pick(pick_id, user_id, vote_option_id) VALUES (34, 5, 4);
insert into pick(pick_id, user_id, vote_option_id) VALUES (35, 5, 6);
insert into pick(pick_id, user_id, vote_option_id) VALUES (36, 5, 8);
insert into pick(pick_id, user_id, vote_option_id) VALUES (37, 5, 10);
insert into pick(pick_id, user_id, vote_option_id) VALUES (38, 5, 12);
insert into pick(pick_id, user_id, vote_option_id) VALUES (39, 5, 14);
insert into pick(pick_id, user_id, vote_option_id) VALUES (40, 5, 16);
