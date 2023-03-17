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