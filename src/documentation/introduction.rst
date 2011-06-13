============
Introduction
============

RESThub is an Open Source framework designed to allow you to build RIA HTML5 web applications following KISS, DRY and REST principles. 

It offers modular sets of functionalities :

* A Java stack including
	* A plugin architecture
	* A generic MIDDLE stack
	* A generic REST webservice stack
	* A optional Tapestry 5 integration for classical MVC framework server based architecture
* A javascript HTML5/Javascript stack for building RIA
* Some documentation and tooling 

.. image:: _static/blocks.png
	:width: 848 px
	:height: 504 px
	:scale: 75 %
	:alt: RESThub functionalities
	:align: center

Requirements
============


Before coding, you should download and install development tools if needed : 

* `Java 6 <http://java.sun.com/javase/downloads/index.jsp>`_
* `Maven 3 <http://maven.apache.org/>`_

Documentation
=============

RESThub Javadoc is available `here <http://resthub.org/javadoc/1.1>`_.

Framework documentation included in RESThub may also be useful :

* `Maven complete reference <http://www.sonatype.com/books/mvnex-book/reference/public-book.html|Maven by example]], [[http://www.sonatype.com/books/mvnref-book/reference/public-book.html>`_
* `Spring 3 reference manual <http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html|html]], [[http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/pdf/spring-framework-reference.pdf>`_
* `Spring 3 javadoc <http://static.springsource.org/spring/docs/3.0.x/javadoc-api/>`_
* `Hibernate documentation <http://www.hibernate.org/docs.html>`_
* `Hades <http://hades.synyx.org/static/2.x/site/org.synyx.hades/apidocs/>`_
* `H2 embedded database <http://www.h2database.com/html/main.html>`_

Project templates
=================

The easiest way to start is to use RESThub archetypes to create your first RESThub based application. Just open a command line terminal, and type :

.. code-block:: bash

	mvn org.apache.maven.plugins:maven-archetype-plugin:2.0:generate -DarchetypeCatalog=http://resthub.org/nexus/content/repositories/releases/

You will have to choose between 3 RESThub archetypes :

* **resthub-archetype-js-webapp** : simple HTML5/Javascript web application
* **resthub-archetype-tapestry5-webapp** : simple RESThub based Tapestry 5 web application
* **resthub-archetype-jar-module** : a JAR module for inclusion in your multi modules project
 
You can run your webapps thanks to builtin mvn jetty:run support.

Sample applications
===================

Check these sample applications to learn how to design your RESThub based web application

* `Roundtable <https://github.com/pullrequest/resthub/tree/master/resthub-apps/roundtable/>`_ : a doodle like clone.
* `Booking JS <https://github.com/pullrequest/resthub/tree/master/resthub-apps/booking/booking-js/>`_ : booking demo application, implemented with Javascript stack
* `Booking Tapestry 5 <https://github.com/pullrequest/resthub/tree/master/resthub-apps/booking/booking-tapestry5/>`_ : booking demo application, implemented with Tapestry 5 integration

In order to test and run one oh these applications :

* Check that Git, Java and Maven are installed
* Clone RESThub repo : git clone https://github.com/pullrequest/resthub.git
* Open command line, and go to one of
	* resthub/resthub-apps/booking/booking/booking-tapestry5
	* resthub/resthub-apps/booking/booking/booking-js
	* resthub/resthub-apps/roundtable
* Run mvn jetty:run
* Open your browser and go to http://localhost:8080
