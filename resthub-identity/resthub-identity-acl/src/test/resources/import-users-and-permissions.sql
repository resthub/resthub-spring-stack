/* login : test , password :  t3st */
insert into permissions_owner (id) values (1);
insert into idm_users (id, firstName, lastName, email, login, password) values (1, 'test', 'ing', 'test@resthub.org', 'test', '{SHA}u3/Rg4+2cdohm4CmQtP9Qq45HX0=');
insert into permissions_owner_permissions (id, permissions) values (1, 'IM-USER');
insert into permissions_owner_permissions (id, permissions) values (1, 'CREATE');

/* login : admin , password : 4dm|n */
insert into permissions_owner (id) values (2);
insert into idm_users (id, firstName, lastName, email, login, password) values (2, 'alex', 'synclar', 'user1@resthub.org', 'admin', '{SHA}gf3KgPW/QIbAMON8QvYID/ep8AY=');
insert into permissions_owner_permissions (id, permissions) values (2, 'IM-ADMIN');