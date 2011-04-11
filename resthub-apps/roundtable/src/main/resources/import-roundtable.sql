insert into poll (id, author, topic, body, creation_date, expiration_date) values (1, 'Me', 'Sample Topic', 'Whats the best day for schedule a meeting ?.', '2010-01-01', '2010-01-01');
insert into poll (id, author, topic, body, creation_date, expiration_date) values (2, 'Me', 'Sample Topic', 'Can you deliver this for me ?', '2010-01-01', '2010-01-01');
insert into poll (id, author, topic, body, creation_date, expiration_date) values (3, 'Me', 'Sample Topic', 'How do you do ?', '2010-01-01', '2010-01-01');
insert into poll (id, author, topic, body, creation_date, expiration_date) values (4, 'Me', 'Misc Topic', 'Whats Your Favorite Color?', '2010-01-01', '2010-01-01');
insert into poll (id, author, topic, body, creation_date, expiration_date) values (5, 'Me', 'Misc Topic', 'What is the air speed velocity of an unladen swallow?', '2010-01-01', '2010-01-01');
insert into poll (id, author, topic, body, creation_date, expiration_date) values (6, 'Me', 'Misc Topic', 'Whate is the universal answer to all ?', '2010-01-01', '2010-01-01');
insert into poll (id, author, topic, body, creation_date, expiration_date) values (7, 'Me', 'Misc Topic', 'Be or not to be that is the question.', '2010-01-01', '2010-01-01');
insert into poll (id, author, topic, body, creation_date, expiration_date) values (8, 'Me', 'Garbage Topic', 'This is an idiot question...', '2010-01-01', '2010-01-01');

insert into answer (id, poll_id, order_num, body) values (1, 1, 1, 'Monday');
insert into answer (id, poll_id, order_num, body) values (2, 1, 2, 'Tuesday');
insert into answer (id, poll_id, order_num, body) values (3, 1, 3, 'Wednesday');
insert into answer (id, poll_id, order_num, body) values (4, 1, 4, 'Thursday');
insert into answer (id, poll_id, order_num, body) values (5, 1, 5, 'Friday');

insert into answer (id, poll_id, order_num, body) values (6, 2, 1, 'YES');
insert into answer (id, poll_id, order_num, body) values (7, 2, 2, 'NO');

insert into answer (id, poll_id, order_num, body) values (8, 3, 1, 'I''m fine, thank you');
insert into answer (id, poll_id, order_num, body) values (9, 3, 2, 'I''m feeling very bad');

insert into answer (id, poll_id, order_num, body) values (10, 4, 1, 'blue');
insert into answer (id, poll_id, order_num, body) values (11, 4, 2, 'blue... no! red');

insert into answer (id, poll_id, order_num, body) values (12, 5, 1, '... i d''ont know!');
insert into answer (id, poll_id, order_num, body) values (13, 5, 2, 'It depends on whether you are talking about an African or European swallow.');

insert into answer (id, poll_id, order_num, body) values (14, 6, 1, '42');
insert into answer (id, poll_id, order_num, body) values (15, 6, 2, 'maybe... 42?');

insert into answer (id, poll_id, order_num, body) values (16, 7, 1, 'To be !');
insert into answer (id, poll_id, order_num, body) values (17, 7, 2, 'Not to be !');
insert into answer (id, poll_id, order_num, body) values (18, 7, 3, 'Anakin Skywalker !');

insert into answer (id, poll_id, order_num, body) values (19, 8, 1, 'yes... you are right');

insert into voter (id, poll_id, name) values (1, 1, 'Doc');
insert into voter (id, poll_id, name) values (2, 1, 'Grumpy');
insert into voter (id, poll_id, name) values (3, 1, 'Happy');
insert into voter (id, poll_id, name) values (4, 1, 'Sleepy');
insert into voter (id, poll_id, name) values (5, 1, 'Bashful');
insert into voter (id, poll_id, name) values (6, 1, 'Dopey');

insert into vote (id, answer_id, voter_id, val) values (1, 1, 1, 'yes');
insert into vote (id, answer_id, voter_id, val) values (2, 2, 1, 'yes');
insert into vote (id, answer_id, voter_id, val) values (3, 3, 1, 'yes');
insert into vote (id, answer_id, voter_id, val) values (4, 4, 1, 'yes');
insert into vote (id, answer_id, voter_id, val) values (5, 5, 1, 'yes');

insert into vote (id, answer_id, voter_id, val) values (6, 1, 2, 'no');
insert into vote (id, answer_id, voter_id, val) values (7, 2, 2, 'no');
insert into vote (id, answer_id, voter_id, val) values (8, 3, 2, 'no');
insert into vote (id, answer_id, voter_id, val) values (9, 4, 2, 'no');
insert into vote (id, answer_id, voter_id, val) values (10, 5, 2, 'no');

insert into vote (id, answer_id, voter_id, val) values (11, 1, 3, 'yes');
insert into vote (id, answer_id, voter_id, val) values (12, 2, 3, 'yes');
insert into vote (id, answer_id, voter_id, val) values (13, 3, 3, 'yes');
insert into vote (id, answer_id, voter_id, val) values (14, 4, 3, 'yes');
insert into vote (id, answer_id, voter_id, val) values (15, 5, 3, 'yes');

insert into vote (id, answer_id, voter_id, val) values (16, 1, 4, 'yes');
insert into vote (id, answer_id, voter_id, val) values (17, 2, 4, 'yes');
insert into vote (id, answer_id, voter_id, val) values (18, 3, 4, 'yes');
insert into vote (id, answer_id, voter_id, val) values (19, 4, 4, 'no');
insert into vote (id, answer_id, voter_id, val) values (20, 5, 4, 'no');

insert into vote (id, answer_id, voter_id, val) values (21, 1, 5, 'yes');
insert into vote (id, answer_id, voter_id, val) values (22, 2, 5, 'no');
insert into vote (id, answer_id, voter_id, val) values (23, 3, 5, 'no');
insert into vote (id, answer_id, voter_id, val) values (24, 4, 5, 'no');
insert into vote (id, answer_id, voter_id, val) values (25, 5, 5, 'no');

insert into vote (id, answer_id, voter_id, val) values (26, 1, 6, 'yes');
insert into vote (id, answer_id, voter_id, val) values (27, 2, 6, 'no');
insert into vote (id, answer_id, voter_id, val) values (28, 3, 6, 'yes');
insert into vote (id, answer_id, voter_id, val) values (29, 4, 6, 'no');
insert into vote (id, answer_id, voter_id, val) values (30, 5, 6, 'yes');
