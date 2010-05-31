insert into identity (id) values (1);
insert into user (firstName, lastName, email, login, password, id) values ('Bob', 'Martin', 'user1@atosorigin.com', 'a185329', 'pass?w0rd', 1);

insert into identity (id) values (2);
insert into user (firstName, lastName, email, login, password, id) values ('Alice', 'Dubois', 'user2@atosorigin.com', 'a185330', 'pass?w0rd', 2);

insert into identity (id) values (4);
insert into user (firstName, lastName, email, login, password, id) values ('Jean', 'Bonneau', 'user3@atosorigin.com', 'a185331', 'pass?w0rd', 4);

insert into identity (id) values (5);
insert into user (firstName, lastName, email, login, password, id) values ('Estelle', 'Ludinard', 'user4@atosorigin.com', 'a185332', 'pass?w0rd', 5);

insert into identity (id) values (3);
insert into usersgroup ( name, id ) values ( 'myGroup', 3 );

insert into user_group ( groups_id , users_id ) values ( 3, 1 );
insert into user_group ( groups_id , users_id ) values ( 3, 2 );