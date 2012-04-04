==============
Project layout
==============

Let's take a look at a typical RESThub based application...

RESThub is a framework that is intended to allow you to develop efficiently webapp projects. RESThub projects follow the "maven standard" project layout :

 * /pom.xml : the Maven configuration file, it defines dependencies, plugins, etc.
 * /src/main/java : your java classes go there
 * /src/main/resources : your xml and properties files go there
 * /src/main/resources/applicationContext.xml : this is your application Spring configuration file. Since we mainly use annotation based configuration, this file will usually be short
 * /src/main/webapp : your HTML, CSS and javascript files go there
 * /src/main/webapp/WEB-INF/web.xml : java webapplication configuration file, mainly used to configure Spring and Jersey servlets and filters

In bigger projects, functionalities are divided in several JAR modules we usually have several JAR modules (customer management, product management ...). Those modules are included as dependencies in a WAR module (implementing, for example, a RESThub-JS web application in /src/main/webapp).  

Optional but useful : you should use RESThub parent pom, as shown below with the resthub-parent artifact, so as to define Maven plugin configuration and dependencies version.
 
pom.xml
=======

.. code-block:: xml

	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
		<modelVersion>4.0.0</modelVersion>
      
      <parent>
        <artifactId>resthub-parent</artifactId>
        <groupId>org.resthub</groupId>
        <version>1.x</version>
      </parent>
    
		<groupId>org.mydomain</groupId>
		<artifactId>myproject</artifactId>
		<packaging>war</packaging>
		<version>1.0-SNAPSHOT</version>

		<dependencies>
			<!-- We use RESThub core classes and dependencies built around Spring 3 -->
			<dependency>
				<groupId>org.resthub</groupId>
				<artifactId>resthub-core</artifactId>
				<version>1.x</version>
			</dependency>
			<!-- We use RESThub web classes and dependencies built around Jersey -->
			<dependency>
				<groupId>org.resthub</groupId>
				<artifactId>resthub-web-server</artifactId>
				<version>1.x</version>
			</dependency>
			<!-- We use RESThub JS stack built around jQuery -->
			<dependency>
				<groupId>org.resthub</groupId>
				<artifactId>resthub-js</artifactId>
				<version>1.x</version>
				<type>war</type>
			</dependency>
		</dependencies>

		<repositories>
			<!-- Contains all RESThub artifacts and transitive dependencies -->		
			<repository>
				<id>resthub</id>
				<url>http://resthub.org/nexus/content/groups/resthub</url>
			</repository>
		</repositories>
	</project>

applicationContext.xml
======================

.. code-block:: xml

   <beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:context="http://www.springframework.org/schema/context" xmlns:resthub="http://www.resthub.org/schema/context"
      xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
      http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd
      http://www.resthub.org/schema/context http://www.resthub.org/schema/context/resthub-context-1.1.xsd">
			
		<!-- Enable bean declaration by annotations, update base package according to your project -->
		<context:annotation-config/>
		<context:component-scan base-package="org.mydomain.myproject" />

		<!-- Scan your JPA entites to make them manage by EntityManager, even if dispatched in various packages -->
		<resthub:include-entities base-package="org.mydomain.myproject.model" />
		
		<!-- Scan your model classes intended to be serialized/unserialized by Jersey -->
		<resthub:include-jaxb-elements base-package="org.mydomain.myproject.model" />
	</beans>

web.xml
=======

.. code-block:: xml

	<?xml version="1.0" encoding="UTF-8"?>
	<web-app version="2.5"
			 xmlns="http://java.sun.com/xml/ns/javaee"
			 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			 xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
			 
	<display-name>My project</display-name>
      
      <!--  In order to disable application context XSD validation -->
      <context-param>
         <param-name>contextClass</param-name>
         <param-value>org.resthub.common.context.ResthubXmlWebApplicationContext</param-value>
      </context-param>
		
		<!-- Configure application context scanning in all dependencies -->
		<context-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath*:resthubContext.xml classpath*:applicationContext.xml</param-value>
		</context-param>
		
		<filter>
			<filter-name>JpaFilter</filter-name>
			<filter-class>org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter</filter-class>
		</filter>
		<filter-mapping>
			<filter-name>JpaFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>
		
		<listener>
			<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
		</listener>
		
		<servlet>
			<servlet-name>Jersey Web Application</servlet-name>
			<servlet-class>com.sun.jersey.spi.spring.container.servlet.SpringServlet</servlet-class>
		</servlet>
		<servlet-mapping>
			<servlet-name>Jersey Web Application</servlet-name>
			<url-pattern>/api/*</url-pattern>
		</servlet-mapping>
		
	</web-app>

Model
=====

We don't provide base resource classe because too much inheritance cause much performance trouble with JPA.
Instead, you can use the following template class to create your own.

.. code-block:: java
	
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.Id;
	import javax.xml.bind.annotation.XmlRootElement;

	@Entity
	@XmlRootElement
	public class Sample {

		private Long id;
		private String name;

		public Sample() {
			super();
		}

		public Sample(String name) {
			super();
			this.name = name;
		}
		
		@Id
		@GeneratedValue
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
	}
