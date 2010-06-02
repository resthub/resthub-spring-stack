insert into poll (id, author, topic, body, creation_date, expiration_date) values (1, 'Test Author', 'Test Topic', 'This is a junit test poll.', '2010-01-01', '2010-01-01');
insert into poll (id, author, topic, body, creation_date, expiration_date) values (2, 'Test Author', 'Test Topic', 'This is a second junit test poll.', '2010-01-01', '2010-01-01');

insert into answer (id, poll_id, order_num, body) values (1, 1, 1, 'A');
insert into answer (id, poll_id, order_num, body) values (2, 1, 2, 'B');
insert into answer (id, poll_id, order_num, body) values (3, 1, 3, 'C');

insert into answer (id, poll_id, order_num, body) values (4, 2, 1, 'YES');
insert into answer (id, poll_id, order_num, body) values (5, 2, 2, 'NO');

insert into voter (id, poll_id, name) values (1, 1, 'Test Voter 1');
insert into voter (id, poll_id, name) values (2, 1, 'Test Voter 2');

insert into vote (id, answer_id, voter_id, val) values (1, 1, 1, 'yes');
insert into vote (id, answer_id, voter_id, val) values (2, 2, 1, 'no');
insert into vote (id, answer_id, voter_id, val) values (3, 3, 1, 'no');
insert into vote (id, answer_id, voter_id, val) values (4, 1, 2, 'yes');
insert into vote (id, answer_id, voter_id, val) values (5, 2, 2, 'yes');
insert into vote (id, answer_id, voter_id, val) values (6, 3, 2, 'no');
