RESThub Spring stack changelog
==============================

This changelog references the major changes (features, important bugs and security fixes) of resthub-spring-stack project.
Some changes break backwards compatibility, check out the UPGRADE section before you upgrade your application.  

To get the full diff between two versions, go to https://github.com/resthub/resthub-spring-stack/compare/resthub-2.0.0...resthub-2.1.0

2.1.0 version (03-15-2013)
--------------------------

Upgrade from 2.0.0
~~~~~~~~~~~~~~~~~~

`Use BoneCP for database connections pool management <https://github.com/resthub/resthub-spring-stack/pull/170>`_ : Replaced commons-dbcp with bonecp (see `why <https://github.com/resthub/resthub-spring-stack/issues/155>`_, and migration documentation).

If you ever customized one of those datasource keys in your database.properties file:

.. code-block

    dataSource.maxActive = 50
    dataSource.maxWait = 1000
    dataSource.poolPreparedStatements = true
    dataSource.validationQuery = SELECT 1

Then they are not used anymore; you should translate those concepts into the new librabry concepts used for database connections pool management, `BoneCP <http://jolbox.com/>`_. You'll probably want to switch from a "max live/wait connections" to a "partition" approach, which is way more efficient.

`Fix inconsistent API in HTTP Client <https://github.com/resthub/resthub-spring-stack/pull/161>`_ : API calls all look like asyncXmlGet, jsonGet...

New features and fixes
~~~~~~~~~~~~~~~~~~~~~~

* `add CORSFilter <https://github.com/resthub/resthub-spring-stack/pull/171>`_ : optional servlet filter that handles cross-origin requests (documentation)
* `update to Spring Framework 3.2GA <https://github.com/resthub/resthub-spring-stack/issues/138>`_
* `Custom JsonView annotations support <https://github.com/resthub/resthub-spring-stack/issues/154>`_ : Customizing JSON serialization using annotations on entities (`documentation <http://resthub.org/spring-stack.html#custom-json-views>`_)
* `New REST API for model validation <https://github.com/resthub/resthub-spring-stack/pull/166>`_ : Server can export BeanValidation constraints to your client application (`documentation <http://resthub.org/spring-stack.html#validation-api>`_)

See `all issues for this release <https://github.com/resthub/resthub-spring-stack/issues?milestone=14&page=1&state=closed>`_.