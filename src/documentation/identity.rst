================
Identity manager
================

Introduction
------------

Identity Manager implements a User/Group/Role management service, that can be used in several ways :

* As a standalone service (it provides a REST API)
* In embedded mode (part of an existing Spring application)
* With or without Spring Security

Model
-----

Indentity Manager is a SOA service that needs its own database. 
Therefore, it has several JPA entities that are its model layer.

* User. Represents a user, and stores its login (unic identifier), first and last names, and password as well
* Group. A group has a name, and can contains users and other groups.
* Role. A role has a name, and can be applied to users and groups.

Users, Groups and Roles can have several permissions, which are just arbitrary strings.
User passwords are encrypted with Jasypt. You can configure the desired algorithm used for password hash (multiple MD5, SHA-1...).

Architecture
------------

Identity Manager is a full autonomous RESTHub application, with its 3 layers :

1. DAO layer
2. Service layer
3. REST Controller layer

CRUD functionalities are provided to allow user/group/roles management, and permissions attributions.
The REST Controller layer is itself protected.

Modules
-------

Identity Manager is a maven multi module project, containing:

* resthub-identity-core: 3 layered SOA service, with model
* resthub-identity-manager: depends on the core, adds a web.xml for deployement in Server Container, and an Javascript RIA application for administration purposes.
* resthub-identity-acl: depends on the core, provides integration with SpringSecurity 3's ACL mecanism.

Identity manager test users are :

* admin / 4dm|n
* test / test

Security
--------

Identity Manager can be turned in an OAuth2 provider, thanks to the resthub-oauth2-spring-security module.
It means that, in a SOA application, Identity Manager centralizes identities and is requested by other services when HTTP request authentication is needed.

As said before, the REST api are protected.
That means that you'll need OAuth2 tokens to access it, token that could be provided by the application itself.

Thus, Identity Manager acts as an OAuth2 provider AND an OAuth2 secured resources.
