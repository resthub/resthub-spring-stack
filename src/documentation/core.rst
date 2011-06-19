=====================
Core functionnalities
=====================

RESThub core has been designed to give your a cohenrent stack and some functionnalities in order to help you rto build your applications faster.

You should import the following module in your application in order to be able to use RESThub in your applications :

.. code-block:: xml

   <dependency>
      <groupId>org.resthub</groupId>
      <artifactId>resthub-core</artifactId>
      <version>1.1</version>
   </dependency>

Functionnalities available from the core are described bellow.

Application context
===================

Since RESThub application context functionnalities extends Spring 3 ones, you should carefully read `Spring 3 reference manual <http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html>`_.

Application context scanning
----------------------------

RESThub default configuration scan all resthubContext.xml and applicationContext.xml files from your classpath. This naming convention and scanning process allow you to build easily a simple plugin like system when your functionalities are automatically discovered if they are in the classpath.

You can provide a applicationContext.xml file by module to configure your application.

Beans declaration and injection
-------------------------------

You should use J2EE6 annotations to declare and inject your beans.

To declare a bean :

.. code-block:: java

   @Named("beanName")
   public class SampleClass {
   
   }

To inject a bean :

.. code-block:: java

   @Inject
   @Named("beanName")
   public void setSampleProperty(...) {
   
   }

**Best practice** : Bean injection on setter is better than on protected or private property because it allows subclasses to override this injection.

Environment specific properties
-------------------------------

There is various way to configure your environment specific properties in your application, the one described bellow is the more simple and flexible one we have found.

Maven filtering (search and replace variables) is not recommanded because it is done at compile time (not runtime) and make usually your JAR/WAR specific to an specific environnement. The only case they are interesting is to define your target path (${project.build.directory}) in for your src/test/applicationContext.xml for testing purpose.

Spring properties placeholders allow you to reference in your application context files some values defined in external properties. This is useful in order to keep your application context generic (located in src/main/resources or src/test/resources), and put all values that depends on the environnement (local, dev, staging, production) in external properties. These dynamic properties values are resolved durin application startup.

In order to improve testabilty and extensibility of your modules, it is a good idea to set default values taken in account if no properties in found in the classpath, and override these default values if the propeties is found. It is acheived by declaring the following lines in your applicationContext.xml :

.. code-block:: xml

   <context:property-placeholder location="classpath*:mymodule.properties"
                                 properties-ref="databaseProperties"
                                 ignore-resource-not-found="true"
                                 ignore-unresolvable="true" />

   <util:properties id="mymoduleProperties" >
      <prop key="param1">param1Value</prop>
      <prop key="param2">param2Value</prop>
   </util:properties>

You should now be able to inject dynamic values like in your beans :

.. code-block:: xml

   <bean id="sampleBean" class="org.mycompany.MyBean">
      <property name="property1" value="${param1}"/>
      <property name="property2" value="${param2}"/>
   </bean>

You can also inject direcly this values in your Java classes thanks to the @Value annotation :

.. code-block:: java

   @Value("#{mymoduleProperties.param1}")
   protected String property1;

Or :

.. code-block:: java

   @Value("#{mymoduleProperties.param1}")
   protected void setProperty1(String property1) {
      this.property1 = property1;
   }


Disable XSD validation
----------------------

By default, Spring 3 validation XML schema declared in your application context. This validation could prevent you to use protperties placeholder decribed previously, because you will put a value like ${paramStatus} in boolean attribute that can take only true or false value.

Since there is no way to fix that in vanilla Spring 3, RESThub provides a way to disable application context XSD validations.

In order to disable validation in your unit tests, annotate your test classes with :

.. code-block:: java

   @ContextConfiguration(loader = ResthubXmlContextLoader.class)

In order to disable validation in your web application, you should declare in the web.xml file (ResthubXmlWebApplicationContex is located in resthub-web-server dependency) :

.. code-block:: xml

   <context-param>
      <param-name>contextClass</param-name>
      <param-value>org.resthub.web.context.ResthubXmlWebApplicationContext</param-value>
   </context-param>

Persistence
===========

Default configuration
---------------------

RESThub comes with a preconfigured Spring/Hibernate stack, with connection H2 embedded databse, pooling and cache. Every configured bean could be customized by defefining the bean in your applicationContext.xml files, or more easily by putting a database.properties in your project resources.

Please find bellow the properties keys and default values of database.properties ::
        
   dataSource.driverClassName = org.h2.Driver
   dataSource.url = jdbc:h2:mem:resthub;DB_CLOSE_DELAY=-1
   dataSource.maxActive = 50
   dataSource.maxWait = 1000
   dataSource.poolPreparedStatements = true
   dataSource.username = sa
   dataSource.password = 

   hibernate.show_sql" = false
   hibernate.dialect = org.hibernate.dialect.H2Dialect
   hibernate.format_sql = true
   hibernate.hbm2ddl.auto = update
   hibernate.cache.use_second_level_cache = true
   hibernate.cache.provider_class = net.sf.ehcache.hibernate.SingletonEhCacheProvider
   hibernate.id.new_generator_mappings = true

Please notice that the new Hibernate id generator is used, as `recommanded in Hibernate documentation <http://docs.jboss.org/hibernate/annotations/3.5/reference/en/html_single/#ann-setup-properties>`_. It allows much more better performances (no need of a select request before an insert request).

Entity scan
-----------

RESthub allow to scan entities in different modules using the same Persitence Unit, which is not possible with default Spring/Hibernate.

By default, the ScanningPersistenceUnitManager searches entities with the pattern.
To indicates differents packages, you'll have to override the bean definition in your own Spring configuration file.

.. code-block:: xml

   <resthub:include-entities base-package="net.myProject.**.model" />

Now, entities from the net/myProject/\*\*/model packages will be recognized.

**Beware !** You have to be careful with the loading order of your spring configuration files.
Reference the RESTHub file first (and don't forget the * behind "classpath"), and then your files.

Interface only DAO
------------------

Hades is a really powerful Generic DAO framework, included by default in RESthub, which allow to write your DAO with only an interface (no implmentation needed).

Hades ability to generate DAO from interfaces is not activated by default in RESThub application, but could be easily by addid the following line in your applicationContext.xml.

.. code-block:: xml

   <hades:dao-config base-package="org.mycompany.myproject.dao" />

Generic CRUD classes
====================

RESThub provides some generic classes in order to quicly implement CRUD functionalities :

Provides some generic classes and interfaces for default DAO, service or controller.

* **Dao** : GenericDao default CRUD operation
   * interface `GenericDao <http://resthub.org/javadoc/1.1/org/resthub/core/dao/GenericDao.html>`_: generic DAO interface
   * class `GenericJpaDao <http://resthub.org/javadoc/1.1/org/resthub/core/dao/GenericJpaDao>`_: generic DAO JPA implementation
* **Generic CRUD services** : RESThub provides a reusable service classes that implements by default CRUD operations.
   * interface `GenericService <http://resthub.org/javadoc/1.1/org/resthub/core/service/GenericService.html>`_: generic service interface
   * class `GenericServiceImpl <http://resthub.org/javadoc/1.1/org/resthub/core/service/GenericServiceImpl.html>`_: generic service default implementation

For example, to define a CRUD interface for Booking class :

.. code-block:: java

   public interface BookingService extends GenericService<Booking, Long> {
   
   }



