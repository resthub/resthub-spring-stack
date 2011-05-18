=================
Project structure
=================

Let's go to see what looks like a typical RESThub based application ...

RESThub is a framework that is intended to allow you to develop efficiently webapp projects. It has Maven the following directory layout :

 * /pom.xml : the Maven configuration file, it defines dependencies, plugins, etc.
 * /src/main/java : your java classes go there
 * /src/main/resources : your xml and properties files go there
 * /src/main/resources/applicationContext.xml : this is your application Spring configuration file. Since we mainly use annotation based configuration, this file will usually be short
 * /src/main/webapp : your HTML, CSS and javascripts files go there
 * /src/main/webapp/WEB-INF/web.xml : java webapplication configuration file, mainly used to configure Spring and Jersey servlet and filters
 
pom.xml
-------

.. code-block:: xml

	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	
		<modelVersion>4.0.0</modelVersion>
		<groupId>org.mydomain</groupId>
		<artifactId>myproject</artifactId>
		<packaging>war</packaging>
		<version>1.1-SNAPSHOT</version>

    	<dependencies>
			<!-- We use RESThub core classes and dependencies built around Spring 3 -->
        	<dependency>
				<groupId>org.resthub</groupId>
				<artifactId>resthub-core</artifactId>
				<version>1.1-SNAPSHOT</version>
			</dependency>
			<!-- We use RESThub web classes and dependencies built around Jersey -->
			<dependency>
				<groupId>org.resthub</groupId>
				<artifactId>resthub-web</artifactId>
				<version>1.1-SNAPSHOT</version>
			</dependency>
			<!-- We use RESThub js stack built around jQuery -->
			<dependency>
				<groupId>org.resthub</groupId>
				<artifactId>resthub-js</artifactId>
				<version>1.1-SNAPSHOT</version>
				<type>war</type>
			</dependency>
		</dependencies>

		<build>
        	<pluginManagement>
				<plugins>
					<!-- Set default compilation to Java 1.5 and UTF-8 for encoding -->
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-compiler-plugin</artifactId>
						<version>2.3</version>
						<configuration>
							<encoding>UTF-8</encoding>
							<source>1.5</source>
							<target>1.5</target>
						</configuration>
					</plugin>
					<!-- It is recommanded to define resource encoding to avoid charset issues -->
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-resources-plugin</artifactId>
						<version>2.4.2</version>
						<configuration>
							<encoding>UTF-8</encoding>
						</configuration>
					</plugin>
				</plugins>
			</pluginManagement>
			<plugins>
				<!-- In order to run easily your webapp -->
				<plugin>
					<groupId>org.mortbay.jetty</groupId>
					<artifactId>jetty-maven-plugin</artifactId>
					<version>7.1.3.v20100526</version>
					<configuration>
						<scanIntervalSeconds>10</scanIntervalSeconds>
					</configuration>
				</plugin>
			</plugins>
		</build>
		<repositories>
			<!-- Contains all RESThub artifacts and transitive dependencies -->		
			<repository>
				<id>resthub</id>
				<url>http://resthub.org/nexus/content/groups/resthub</url>
			</repository>
		</repositories>
	</project>

applicationContext.xml
----------------------

.. code-block:: xml

	<beans	xmlns="http://www.springframework.org/schema/beans"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xmlns:context="http://www.springframework.org/schema/context"
			xmlns:tx="http://www.springframework.org/schema/tx"
			xmlns:jdbc="http://www.springframework.org/schema/jdbc"
			xmlns:p="http://www.springframework.org/schema/p"
			xsi:schemaLocation="http://www.springframework.org/schema/beans
			http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
			http://www.springframework.org/schema/context
			http://www.springframework.org/schema/context/spring-context-3.0.xsd
			http://www.springframework.org/schema/tx
			http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">
	
		<!-- Enable bean declaration by annotations, update base package according to your project -->
		<context:annotation-config/>
		<context:component-scan base-package="org.mydomain.myproject" />
		
		<!-- You will have to customize classpathPatterns -->
		<bean id="scanningPersistenceUnitManager" class="org.resthub.core.dao.jpa.ScanningPersistenceUnitManager">
			<property name="defaultDataSource" ref="dataSource" />
			<property name="classpathPatterns" value="classpath*:org/resthub/core/model/Resource.class;classpath:org/mydomain/myproject/model/**/*.class" />
		</bean>	
	</beans>

web.xml
-------

.. code-block:: xml

	<?xml version="1.0" encoding="UTF-8"?>
	<web-app version="2.5"
			 xmlns="http://java.sun.com/xml/ns/javaee"
			 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			 xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
			 
		<display-name>My project</display-name>
		
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
   public class SampleResource {
      
       private static final long serialVersionUID = -7178337784737750452L;
   
       private Long id;
       private String name;
   
       public WebSampleResource() {
           super();
       }
   
       public WebSampleResource(String name) {
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
       
       @Override
       public boolean equals(Object obj) {
           if (obj == null) {
               return false;
           }
           if (getClass() != obj.getClass()) {
               return false;
           }
           final Resource other = (Resource) obj;
           if ((this.id == null) ? (other.getId() != null) : !this.id.equals(other.getId())) {
               return false;
           }
           
           return true;
       }
   
       @Override
       public int hashCode() {
           int hash = 3;
           hash = 43 * hash + (this.id == null ? 0 : this.id.hashCode());
           return hash;
       }
   
      @Override
      public String toString() {
         return "WebSampleResource[" + getId() + ","+ getName() + "]";
      }
   }

