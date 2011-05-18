================
Identity manager
================

Introduction
------------

A WebService that serves data is good. But a WebService that serves data securly is better.
That's the purpose of the OAuth2 implementation RESThub provides.

But the OAuth2 module only holds token management, and does not provide any way to manage users.
That is the goal of Identity Manager.

Identity Manager implements a User/Group/Role management service, that can be used in several ways :
- As a standalone service (it provides a REST API)
- In embedded mode (part of an existing Spring application)
- With or without Spring Security

Identity Manager holds the Authentication and Accounting parts of the AAA, while RESThub-OAuth2 w/o SpringSecurity holds the Authorization part.

Model
-----

Indentity Manager is a SOA service that needs its own database. 
Therefore, it has several JPA entities that are its model layer.
- User. Represents a user, and stores its login (unic identifier), first and last names, and password as well
- Group. A group has a name, and can contains users and other groups.
- Role. A role has a name, and can be applied to users and groups.

Users, Groups and Roles can have several permissions, which are just arbitrary strings.
User passwords are encrypted with Jasypt. You can configure the desired algorithm used for password hash (multiple MD5, SHA-1...)

Architecture
------------

Identity Manager is a full autonomous RESTHub applications, with its 3 layers :
1. DAO layer
2. Service layer
3. REST Controller layer

CRUD functionnalities are provided to allow user/group/roles management, and permissions attributions.
The REST Controller layer is itself protected.

Packaging
---------

Identity Manager is a maven multi module project, containing:
- RESThub-IdentityManager-core: 3 layered SOA service, with model
- RESThub-IdentityManager-webapp: depends on the core, add web.xml for deployement in Server Container, and an Javascript RIApplication for administration purposes.
- RESThub-IdentityManager-acl: depends on the core, provide integration with SpringSecurity 3's ACL mecanism.

Security
--------

Identity Manager is an OAuth2 identity provider. 
Its means that, in a SOA application, Identity Manager centralizes identities, and is requested by other services when HTTP request authentication is needed.

Therefore, Identity Manager can act as an SSO provider for your SOA application.

As said before, the REST api are protected.
That means that you'll need OAuth2 tokens to access it, token that could be provided by the application itself.

Thus, IdentityManager acts as an OAuth2 provider AND an OAuth2 secured resources.

