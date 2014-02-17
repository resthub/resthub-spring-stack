# RESThub Spring stack changelog

This changelog references the major changes (features, important bugs and security fixes) of resthub-spring-stack project.
Some changes break backwards compatibility, check out the UPGRADE section before you upgrade your application.  

## 2.1.4 version (12-20-2013)

* Dependencies upgrade
  * Spring from 3.2.4 to 3.2.6
  * Spring Data Commons from 1.5.3 to 1.6.3
  * Spring Data JPA from 1.3.5 to 1.4.3
  * Spring Data MongoDB from 1.2.4 to 1.3.3
  * H2 from 1.3.173 to 1.3.174
  * Jackson from 2.2.3 to 2.3.0
  * Woodstox from 4.1.5 to 4.2.0
  * Hibernate from 4.2.6 to 4.3.0
  * Model Mapper from 0.6.1 to 0.6.2
* Bug fixes
  * [Json serialization of Page<T> when the web service is annoted with a reponseView](https://github.com/resthub/resthub-spring-stack/issues/209)
  * [Empty answers on validation request](https://github.com/resthub/resthub-spring-stack/issues/206)

## 2.1.3 version (12-16-2013)

* Dependencies upgrade
  * Spring from 3.2.3 to 3.2.4
  * Spring Data Commons from 1.5.2 to 1.5.3
  * Spring Data JPA from 1.3.4 to 1.3.5
  * Spring Data MongoDB from 1.2.2 to 1.2.4
  * Spring Security OAuth2 from 1.0.1 to 1.0.5
  * Maven jetty plugin from 8.1.11.v20130520 to 8.1.13.v20130916
  * TestNG from 6.8.5 to 6.8.7
  * H2 from 1.3.171 to 1.3.173
  * Async Http Client from 1.7.18 to 1.7.20
  * Jackson from 2.1.5 to 2.2.3
  * Woodstox from 4.1.4 to 4.1.5 
  * Hibernate from 4.1.12 to 4.2.6
  * Model Mapper from 0.5.6 to 0.6.1
* Bug fixes
  * [Adding swagger-springmvc cause NPE in PostInitializerRunner.java:42](https://github.com/resthub/resthub-spring-stack/issues/214)

## 2.1.2 version (07-26-2013)

* HTTP client improvements
  * Allow to specify charset in HTTP client
  * Manage properly UTF-8
  * Fix a Bug with asynchttpclient using SSL + proxies
* Dependencies upgrade
  * Spring from 3.2.2 to 3.2.3
  * Spring Data JPA from 1.3.0 to 1.3.4
  * Spring Data MongoDB from 1.2.0 to 1.2.1
  * TestNG from 6.8 to 6.8.5
  * Hibernate from 4.1.11 to 4.1.12
  * EHCache from 2.6.5 to 2.6.6
  * Jackson from 2.1.4 to 2.1.5
* Tutorial fixes
  
## 2.1.1 version (05-17-2013)

* Fix BoneCP version and configuration
* Upgrade slf4j, logback, jackson, model mapper, async http client, hibernate, hibernate validator, ehcache, spring data, jetty and fest assert dependencies

## 2.1.0 version (03-15-2013)

### Upgrade from 2.0.0

[Use BoneCP for database connections pool management](https://github.com/resthub/resthub-spring-stack/pull/170) : Replaced commons-dbcp with bonecp (see [why](https://github.com/resthub/resthub-spring-stack/issues/155), and migration documentation).

If you ever customized one of those datasource keys in your database.properties file:

.. code-block

    dataSource.maxActive = 50
    dataSource.maxWait = 1000
    dataSource.poolPreparedStatements = true
    dataSource.validationQuery = SELECT 1

Then they are not used anymore; you should translate those concepts into the new librabry concepts used for database connections pool management, `BoneCP <http://jolbox.com/>`_. You'll probably want to switch from a "max live/wait connections" to a "partition" approach, which is way more efficient.

[Fix inconsistent API in HTTP Client](https://github.com/resthub/resthub-spring-stack/pull/161) : API calls all look like asyncXmlGet, jsonGet...

### New features and fixes

* [add CORSFilter](https://github.com/resthub/resthub-spring-stack/pull/171) : optional servlet filter that handles cross-origin requests (documentation)
* [update to Spring Framework 3.2GA](https://github.com/resthub/resthub-spring-stack/issues/138)
* [Custom JsonView annotations support](https://github.com/resthub/resthub-spring-stack/issues/154) : Customizing JSON serialization using annotations on entities ([documentation](http://resthub.org/spring-stack.html#custom-json-views))
* [New REST API for model validation](https://github.com/resthub/resthub-spring-stack/pull/166) : Server can export BeanValidation constraints to your client application ([documentation](http://resthub.org/spring-stack.html#validation-api))

See [all issues for this release](https://github.com/resthub/resthub-spring-stack/issues?milestone=14&page=1&state=closed).
