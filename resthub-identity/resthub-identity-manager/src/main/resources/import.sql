/* login : test , password :  t3st */
insert into permissions_owner (id) values (1);
insert into idm_users (id, firstName, lastName, email, login, password) values (1, 'test', 'ing', 'test@resthub.org', 'test', 'lvxWgeRR8QOakoGHnYAdxmW1ujQaauua');
insert into permissions_owner_permissions (id, permissions) values (1, 'IM-USER');

/* login : admin , password : 4dm|n */
insert into permissions_owner (id) values (2);
insert into idm_users (id, firstName, lastName, email, login, password) values (2, 'alex', 'synclar', 'user1@resthub.org', 'admin', 'nwCxqV005XMHxW4oPNWmZE5MNXqH31sJ');
insert into permissions_owner_permissions (id, permissions) values (2, 'IM-ADMIN');