# RESThub Spring stack changelog

This changelog references the major changes (features, important bugs and security fixes) of resthub-spring-stack project.
Some changes break backwards compatibility, check out the UPGRADE section before you upgrade your application.  

## 2.1.6 version (02-24-2014)

### New features

* [Alternative to mvc:annotation-driven and params with dots improvements](https://github.com/resthub/resthub-spring-stack/issues/217)
* [Implement a findByIDs method](https://github.com/resthub/resthub-spring-stack/issues/132)

### Fixes

* [Print the complete exception stack when captured by the LoggingHandler](https://github.com/resthub/resthub-spring-stack/pull/226)
* [Remove Hibernate provider warning](https://github.com/resthub/resthub-spring-stack/issues/218)

### Changes

* [Remove unnecessary downcasting from Iterable to List in CrudService](https://github.com/resthub/resthub-spring-stack/issues/228)
* [ModelMapper as optional dependency](https://github.com/resthub/resthub-spring-stack/issues/230) : Be aware that this change could
  potentially break your build. You now have to explicitly include modelmapper as described [here](docs/spring/web-server/#modelmapper)

### Dependency updates

* Principal dependencies
    * spring from 3.2.6 to 3.2.7
    * spring-data-commons from 1.6.3 to 1.6.4
    * logback from 1.0.13 to 1.1.1
    * slf4j from 1.7.5 to 1.7.6
    * modelmapper from 0.6.2 to 0.6.3
    * hibernate.validator from 4.3.1 to 5.0.3
    * javax.validation from 1.0.0 to 1.1.0
    * cglib from 2.2.2 to 3.1
    * hibernate from 4.3.0 to 4.3.1 (fixes [#227](https://github.com/resthub/resthub-spring-stack/issues/227))
    * h2 from 1.3.174 to 1.3.175
    * ehcache from 2.6.6 to 2.6.8
    * async.http.client from 1.7.20 to 1.8.3
    * cors.filter from 1.3.2 to 1.9
    * jackson from 2.3.0 to 2.3.1

* due to javax.validation and hibernate-validator upgrades, two new dependencies were added :
    * javax.el-api 3.0.0
    * el-ri 1.0

* maven plugins:
    * jetty-maven-plugin from 8.1.13.v20130916 to 9.1.2.v20140210
    * test-jetty-servlet from 8.1.13.v20130916 to 8.1.14.v20131031
    * maven-compiler-plugin from 2.5.1 to 3.1
    * maven-failsafe-plugin.version from 2.12.4 to 2.16
    * maven-site-plugin.version from 3.2 to 3.3
    * maven-war-plugin.version from 2.3 to 2.4
    * maven-javadoc-plugin.version from 2.9 to 2.9.1
    * maven-deploy-plugin.version from 2.7 to 2.8.1
    * maven-release-plugin.version from 2.3.2 to 2.4.2
    * maven-install-plugin.version from 2.4 to 2.5.1
    * maven-assembly-plugin.version from2.3  to 2.4
    * maven-project-info-reports-plugin.version from 2.5.1 to 2.7
    * maven-surefire-report-plugin.version from 2.12.14 to 2.16

## 2.1.5 version

Due to incorrect versions and partially failed release, 2.1.5 version is invalid and should not be used.

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

```
dataSource.maxActive = 50
dataSource.maxWait = 1000
dataSource.poolPreparedStatements = true
dataSource.validationQuery = SELECT 1
```

Then they are not used anymore; you should translate those concepts into the new librabry concepts used for database connections pool management, [BoneCP](http://jolbox.com/). You'll probably want to switch from a "max live/wait connections" to a "partition" approach, which is way more efficient.

[Fix inconsistent API in HTTP Client](https://github.com/resthub/resthub-spring-stack/pull/161) : API calls all look like asyncXmlGet, jsonGet...

### New features and fixes

* [add CORSFilter](https://github.com/resthub/resthub-spring-stack/pull/171) : optional servlet filter that handles cross-origin requests (documentation)
* [update to Spring Framework 3.2GA](https://github.com/resthub/resthub-spring-stack/issues/138)
* [Custom JsonView annotations support](https://github.com/resthub/resthub-spring-stack/issues/154) : Customizing JSON serialization using annotations on entities ([documentation](http://resthub.org/spring-stack.html#custom-json-views))
* [New REST API for model validation](https://github.com/resthub/resthub-spring-stack/pull/166) : Server can export BeanValidation constraints to your client application ([documentation](http://resthub.org/spring-stack.html#validation-api))

See [all issues for this release](https://github.com/resthub/resthub-spring-stack/issues?milestone=14&page=1&state=closed).
