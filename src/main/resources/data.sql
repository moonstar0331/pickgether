insert into user(user_Id, user_password, email, nickname, memo, birthday, created_at)
values ('user', '{noop}password', 'email@email.com', 'nick', 'memo', now(), now());

-- 게시글 수정
insert into vote(category, content, create_at, display_range, expired_at, is_multi_pick, modified_at, title, user_id)
values('WORRY', 'This is #content', now(), 'FRIEND', now(), false, now(), 'title', 'user');

insert into vote_option(content, image_link, vote_id)
values('new option1', '/link/image1.png', 1);

insert into vote_option(content, image_link, vote_id)
values('new option2', '/link/image2.png', 1);

insert into hashtag(content)
values('content');

insert into vote_hashtag(hashtag_id, vote_id)
values(1, 1);