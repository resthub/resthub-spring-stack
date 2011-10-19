======================
Tapestry 5 integration
======================

RESThub provides some simple tools to facilitate integration between RESThub and its tools and `Tapestry5 Framework <http://tapestry.apache.org/index.html>`_. 
These tools are packaged into a dedicated `multi-module <https://github.com/pullrequest/resthub/tree/resthub-1.1/resthub-tapestry5>`_.

This multi-module resthub sub-project contains 4 modules related to Tapestry5+RESThub integration.

You can find a complete example of usage of these tools in the sample application : https://github.com/pullrequest/resthub/tree/resthub-1.1/resthub-apps/booking/booking-tapestry5


Tapestry 5 components
=====================

This artefact provides some additional and useful Tapestry5 components and mixins (not directly related to resthub).

For the moment, one unique component  : 
 * a **ZoneUpdater** allowing to update dynamically in AJAX a page zone from a form field. See full usage sample here : `java class <https://github.com/pullrequest/resthub/tree/resthub-1.1/resthub-apps/booking/booking-tapestry5/src/main/java/org/resthub/booking/webapp/t5/pages/Search.java>`_  and `tml <https://github.com/pullrequest/resthub/tree/resthub-1.1/resthub-apps/booking/booking-tapestry5/src/main/resources/org/resthub/booking/webapp/t5/pages/Search.tml>`_

This artefact can and should hold some other components if needed.

Configuration
-------------

To use it, add to your pom dependencies : 

.. code-block:: xml

   <dependency>
      <groupId>org.resthub</groupId>
      <artifactId>resthub-tapestry5-components</artifactId>
      <version>1.1</version>
   </dependency>

Tapestry 5 JPA
==============

This artefact provides some advanced features related to tapestry5 and jpa integration, one of the core resthub tool. This artefact is based on the `Tynamo
Tapestry5 JPA integration <http://tynamo.org/tapestry-jpa+guide>`_

It allows, for example, to manage JPA entities objects in page and components parameters while seeing only id in url. This is allowed by the fact that
the artifact maintains a opened session and wrap calls to serialization and deserialization in order to retrieve an object from its id. 
Sample `here <https://github.com/pullrequest/resthub/blob/master/resthub-apps/booking/booking-tapestry5/src/main/java/org/resthub/booking/webapp/t5/components/hotel/HotelDisplay.java>`_

This artefact provides also a complete integration between JPAEntityManager managed by Tapestry and those defined by Spring, another core tool of resthub.
This is made possible by a `dedicated configuration Tapestry 5 Module 
<https://github.com/pullrequest/resthub/blob/master/resthub-tapestry5/resthub-tapestry5-jpa/src/main/java/org/resthub/tapestry5/jpa/services/ResthubJPAModule.java>`_ 
injecting the `entityManagerFactory` bean, considered as already defined by Spring.

You can provide your own module in your application and complete or override this one in case of specific configurations.

Configuration
-------------

To use it, add to your pom dependencies : 

.. code-block:: xml

   <dependency>
      <groupId>org.resthub</groupId>
      <artifactId>resthub-tapestry5-jpa</artifactId>
      <version>1.1</version>
   </dependency>

Tapestry 5 spring security
==========================

This artefact provides some advanced features related to tapestry5 and spring security integration. This artefact is based on the `localhost.nu
Tapestry5 Spring Security integration <http://www.localhost.nu/java/tapestry-spring-security/conf.html>`_.

It allows to integrate Tapestry5 and Spring authentication and authorization mechanisms and provide some helpers and dedicated services
in order to manage in Tapestry5 pages :

* **login** : 

  wrap login methods and allow to perform a programmatic login : samples `here <https://github.com/pullrequest/resthub/blob/master/resthub-apps/booking/booking-tapestry5/src/main/java/org/resthub/booking/webapp/t5/pages/Signup.java>`_
  and `here <https://github.com/pullrequest/resthub/blob/master/resthub-apps/booking/booking-tapestry5/src/main/java/org/resthub/booking/webapp/t5/components/Layout.java>`_ (see `Authenticator`)

* **signin** : 

  wrap signin methods and allow to perform a programmatic signin : `Sample <https://github.com/pullrequest/resthub/blob/master/resthub-apps/booking/booking-tapestry5/src/main/java/org/resthub/booking/webapp/t5/pages/Signin.java>`_

* **logout** :

  wrap logout methods and allows to perform a programmatic logout : `Sample <https://github.com/pullrequest/resthub/blob/master/resthub-apps/booking/booking-tapestry5/src/main/java/org/resthub/booking/webapp/t5/components/Layout.java>`_

This artifact provides also some security components to use in your tml (see `here <http://www.localhost.nu/java/tapestry-spring-security/ref/index.html>`_).

This is configured by a dedicated Tapestry5 Spring Security configuration that you have to add to your application (see `here <https://github.com/pullrequest/resthub/blob/master/resthub-apps/booking/booking-tapestry5/src/main/java/org/resthub/booking/webapp/t5/services/BookingSecurityModule.java>`_ for a sample).
This configuration file defines all options fo spring security integration.

Configuration
-------------

To use it, add to your pom dependencies : 

.. code-block:: xml

   <dependency>
      <groupId>org.resthub</groupId>
      <artifactId>resthub-tapestry5-spring-security</artifactId>
      <version>1.1</version>
   </dependency>

Tapestry 5 integration
======================

This artifact is a simple wrapper to integrate other Resthub Tapestry5 contributions to make things easier.

It provides a global `Tapestry5 configuration module <https://github.com/pullrequest/resthub/blob/master/resthub-tapestry5/resthub-tapestry5-integration/src/main/java/org/resthub/tapestry5/services/ResthubModule.java>`_
to integrate tapestry-jpa and tapestry-components modules. 

This artifact also include the configuration for Tapestry5 and BeanValidation integration : see `here <https://github.com/pullrequest/resthub/blob/master/resthub-tapestry5/resthub-tapestry5-integration/src/main/java/org/resthub/tapestry5/validation/services/ResthubValidationModule.java>`_.

This inclusion is possible thanks to the `@SubModule` annotation.

This artifact does not integrate tapestry-spring-security to not force its usage.

You can configure the integration of this artifact by providing in your application a dedicated Tapestry5 module and/or overriding these modules
(see `sample <https://github.com/pullrequest/resthub/tree/resthub-1.1/resthub-apps/booking/booking-tapestry5/src/main/java/org/resthub/booking/webapp/t5/services>`_).

Configuration
-------------

To use it, add to your pom dependencies : 

.. code-block:: xml

   <dependency>
      <groupId>org.resthub</groupId>
      <artifactId>resthub-tapestry5-integration</artifactId>
      <version>1.1</version>
   </dependency>

