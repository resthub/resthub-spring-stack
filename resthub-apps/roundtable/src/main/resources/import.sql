insert into poll (id, author, topic, body, creation_date, expiration_date) values (1, 'Me', 'Sample Topic', 'What''s the best day for schedule a meeting ?.', '2010-01-01', '2010-01-01');
insert into poll (id, author, topic, body, creation_date, expiration_date) values (2, 'Me', 'Sample Topic', 'Can you deliver this for me ?', '2010-01-01', '2010-01-01');

insert into answer (id, poll_id, order_num, body) values (1, 1, 1, 'Monday');
insert into answer (id, poll_id, order_num, body) values (2, 1, 2, 'Tuesday');
insert into answer (id, poll_id, order_num, body) values (3, 1, 3, 'Wednesday');
insert into answer (id, poll_id, order_num, body) values (4, 1, 4, 'Thursday');
insert into answer (id, poll_id, order_num, body) values (5, 1, 5, 'Friday');

insert into answer (id, poll_id, order_num, body) values (6, 2, 1, 'YES');
insert into answer (id, poll_id, order_num, body) values (7, 2, 2, 'NO');

insert into voter (id, poll_id, name) values (1, 1, 'John Doe');
insert into voter (id, poll_id, name) values (2, 1, 'Me');

insert into vote (id, answer_id, voter_id, val) values (1, 1, 1, 'yes');
insert into vote (id, answer_id, voter_id, val) values (2, 2, 1, 'no');
insert into vote (id, answer_id, voter_id, val) values (3, 3, 1, 'no');
insert into vote (id, answer_id, voter_id, val) values (4, 4, 1, 'yes');
insert into vote (id, answer_id, voter_id, val) values (5, 5, 1, 'yes');
insert into vote (id, answer_id, voter_id, val) values (6, 1, 2, 'no');
insert into vote (id, answer_id, voter_id, val) values (7, 2, 2, 'no');
insert into vote (id, answer_id, voter_id, val) values (8, 3, 2, 'yes');
insert into vote (id, answer_id, voter_id, val) values (9, 4, 2, 'yes');
insert into vote (id, answer_id, voter_id, val) values (10, 5, 2, 'no');
