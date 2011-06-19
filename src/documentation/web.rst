====================
Web functionnalities
====================

RESThub web support is mainly based on `Jersey framework <http://jersey.java.net/>`_, the reference implementation of JAX-RS standards. It allows to build efficiently REST webservices in Java.

You should import the following module in your application in order to be able to use it :

.. code-block:: xml

   <dependency>
      <groupId>org.resthub</groupId>
      <artifactId>resthub-web-server</artifactId>
      <version>1.1</version>
   </dependency>

Generic controllers
===================

RESThub comes with a Generic REST controler that allows to setup a CRUD webservice in a few lines : `GenericControllerImpl <http://resthub.org/javadoc/1.1/org/resthub/web/controller/GenericControllerImpl.html>`_.

For example :

.. code-block:: java

    @Path("/booking")
    @Named("bookingController")
    public class BookingController extends GenericControllerImpl<Booking, Long, BookingService> {

    }

Spring integration
==================

Spring support is provided by default. It allows to scan automatically your Spring beans, and recognize them as REST webserices if they are annotated with @Path.

Be careful when using transactional or secured controllers, Spring integration currently supports only CGLIB based proxy, not JDK dynamic proxies. For example, if you use Spring Security whith annotations on your controllers, you will have to specify in your application context :

.. code-block:: xml

    <!-- proxy-target-class set to true in order to get cglib proxy and not jdk dynamic proxy (Jersey compatible with only cglib proxies)-->
    <security:global-method-security proxy-target-class="true"/>

Serialization
=============

Default configuration
---------------------

RESThub comes with builtin XML and JSON support for serialization.

XML serialization/deserialization is based on default JAXB Jersey support.

JSON serialization/deserialization is based on `Jackson framework <http://jackson.codehaus.org/>`_, wich is much more powerfull and flexible than default Jersey JSON support. Please read `Jackson annotation guide <http://wiki.fasterxml.com/JacksonAnnotations>`_ for details about configuration capabilities.

Jackson support for JAXB annotations is disabled by default, because they cause a lot of confusion between XML/JSON serialization/deserialization.


Include JAXB elements
---------------------

RESThub provides helpers in order to add in JAX-RS context classes returns as generic collection like List<MyObject> because of type erasure issue in Java, Jersey has no way to know your class MyObject and you will get an exception when trying to serialize it.

In order to make your class recognize by Jersey, add in your applicationContext.xml:

.. code-block:: xml

    <resthub:include-jaxb-elements base-package="net.myProject.**.model"/>

REST Client
===========

REST client provided by RESthub is Jersey Client with Apache HTTP Client 4.x integration. In order to use it, just the the following lines to your pom.xml (resthub-web-server is not needed) :

.. code-block:: xml

   <dependency>
      <groupId>org.resthub</groupId>
      <artifactId>resthub-web-client</artifactId>
      <version>1.1</version>
   </dependency>

Yo use it, just use the client factory, it will create an Http Client instance preconfigured with the right Json support and serialization stuff :

.. code-block:: java

    HttpClient httpClient = ClientFactory.create();
