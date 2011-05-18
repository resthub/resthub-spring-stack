.. highlight:: java

========================
OAuth2 based AuthN/AuthZ
========================

Introduction
============

When designing distributed applications, the security concerns become quickly a big deal.

Through RESThub, we propose a simple way to deal with these concerns. but first just make some clarification.

Authentication
--------------

is the process to clearly identify who is behind an HTTP request. 
It requires to have an account somewhere (on an Authentication server).
The account is authentified by a unic identifier (in the application).

The main difficulties about authentication are
* the security level of credential
* to get a "Single Sign On", which is the ability to log in the distributed application once, and never again (until the "session" expired), even when requesting new service in the application.

Authorization
-------------

covers ways to associate a specific user with a set of permissions upon the application's resources.
Users are given roles, and roles have multiple permissions.
Permissions may be overriden for single users.

When dealing with authorization, the main questions are
* What is the permission's granularity?
* should permissions centralized, like roles? 

Accounting
----------

wraps general informations (generally specific to your application) about users.


When specification helps us
===========================

When building a distributed application with RESThub, your client is a web browser, and this client will propably access directly your REST services.

This is not a new deal: Facebook, Twitter, Google, Yahoo (only to name a  few) already answered these matters.

As we don't want to reinvent the wheel, we choose to use the fruits of their work: the OAuth 2.0 specification.
This specification describes workflows between Authorization Servers, Resource Servers, Resource owner and Client.
As it covers a very large panel of cases, we choose to focus on those relevant to our design.

Some definitions:
* //Resource owner:// Something (someone) who can grant access to a business object (a resource). The end-user is the resource owner over all it's related business objects.
* //Resource server:// The REST Service which hosts a business object (a resource).
* //Client:// Server, browser, mobile application or standalone application which want to get access to a resource.
* //Authorization server://The REST Service which can decide if a client has access to a resource on behalf of a Resource owner.

AAA on RESThub
==============

Principles
----------

What do we need?
* A Single Sign On: we want the user to authenticate only once, and never again.
* A secured and efficient solution: credentials will only be exchanged once, and never again. Access on a resource server is secured and the security checks do not heavily impact runtime performances.
* An unic-overall access: users will be grant access on the whole distributed application.
* A fine-grained authorization: each Resource server has to maintain its own permissions
* A centralized and specialized accounting: we need to plug the accounting part on your own business users management service.

Summary
-------

We can resume all by a diagram:

.. image:: ../_static/oauth-1.png

Concretly
---------

We will thus provide:
* A java authorization server layer, which you have to plug ahead your centralized accounting service.
* A java servlet resource server filter, which must be deployed on each services
* A javascript client toolkit, to performs authorization request, stores tokens, enrich further calls with those tokens, and manage security protocol errors.

In a chronologic order:
# the javascript toolkit will helps you perform the authorization request, with your user credentials.
# the autorization server layer will delegate to our accounting service the authentication part.
# once the user authenticated, the authorization server layer will returned a time-limited token
# the javascript toolkit will stores this token, and enrich your further calls to the REST services.
# on each REST services, the resource server filter will check the token validity, by requesting the central accounting service.
# the autorization server layer will delegate to out accounting service the retrieving of the user permissions, related to the incoming token. For further calls, results will be stored in a Memcache's distributed  cache.
# the resource server filter will enrich a spring security "Principal" object with the retrieved permission, allowing you to managed our business security.
# you can access to the authenticated user id, stored in a request header "user_id"

Usage
=====

Authorization Server
--------------------

All you've got to provide is the Auth**entication** part.
RESThub will deal the Auth**orization** one, and use your own specific identity management.

So you'll have to write the Authentication Service bean.

Authentication Service bean
~~~~~~~~~~~~~~~~~~~~~~~~~~~

This bean must implements the following interface::
	
	package org.resthub.oauth2.provider.service;
	
	import java.util.List;
	
	public interface AuthenticationService {
	
		/**
	 	 * Returns the user's unic identifier from its user name and password.
	 	 * 
	 	 * @param userName The searched user name.
	 	 * @param password The corresponding password.
	 	 * @return Identifier of the user account. Null if no account found.
	 	 */
		String getUser(String userName, String password);
	
		/**
		 * Returns the user's permissions.
		 * 
		 * @param userId The user's identifier. Must not be null.
		 * @return The corresponding permissions. May be empty, but not null.
		 */
		List<String> getUserPermissions(String userId);
	
	} // interface AuthenticationProvider

You'll be totally free to manage your users and their roles, permission, and so on.
The only thing needed by RESThub's Authorization service is the user's permissions.
These strings will be used as JSR-250 "roles". We'll see later.

Authorization Service bean
~~~~~~~~~~~~~~~~~~~~~~~~~~

To use the RESThub's Authorization service, just past these lines in a Spring configuration file::

.. code-block:: xml

	<!-- Scan Spring IOC annotations for oauth2 RESThub beans -->
	<context:component-scan base-package="org.resthub.oauth2" />
	
	<!-- Declaration of the authorization service -->
	<bean name="authorizationService" class="org.resthub.oauth2.provider.service.AuthorizationServiceImpl">
		<property name="dao" ref="tokenDao"/>
		
		<!-- You configure here the lifetime of generated tokens, in seconds  -->
		<property name="tokenLifeTime" value="500" />

		<!-- The number of random character a token will contain -->
		<property name="tokenLength" value="15" />
		
		<property name="authenticationProviders">
			<list>
				<!-- Referenced here your own Authentication bean -->
				<bean ref="myOwnAuthenticationService"/>
			</list>
		</property>
	</bean>

	<!-- Security configuration -->
	<util:properties id="securityConfig">
		<!-- These password is used to protect request between the Authorization Server and Resources servers. -->
		<prop key="authorizationPassword">p@ssw0rd</prop>
	</util:properties>
	
This bean use your Authentication service to check incoming username/password, and generate a random string (the access-token), with a limited lifetime.
This access token is stored in DB, associated to the returned permissions, and the user id.

You can extend (or remplace) the org.resthub.oauth2.provider.service.AuthorizationServiceImpl class to generate tokens with your own mecanism (add some encryption for example)

REST API
~~~~~~~~

We provide a REST OAuth 2 endpoint to obtain a token with a given username/password:

	POST http(s)://XXX.XXX.XXX.XXX:PPPP/YYYYYY/authorize/token

See `Oauth 2 specification <http://tools.ietf.org/html/draft-ietf-oauth-v2-10#section-4>`_ for details.

And a spacific end point for Resource server that want to obtain informations on token:\\

	GET http(s)://XXX.XXX.XXX.XXX:PPPP/YYYYYY/authorize/tokenDetails

Use the 'Authorization' header to pass a password (the one configured in conf file)
Use the 'access_token' query parameter to pass the access token from whom you need details.
Your answer will be

.. code-block:: xml

	{
		"accessToken":"1C1W9aW4XfL02xl",
		"userId":"toto",
		"createdOn":1279719412405,
		"lifeTime":500,
		"refreshToken":"Quc6p619fpezsRA",
		"permissions":["ADMIN"],
		"id":1
	}

(Serialization is org.resthub.oauth2.provider.model.Token class).

Secured your Resource servers
-----------------------------

We provide a Servlet filter to enforce security on your Resource Servers, because thought this is the least intrusive solution, at least for Servlet servers.\\
It's plan to provide an Httpd or NGinX module to secure static/cgi resources, like php scripts

Servlet Filter
~~~~~~~~~~~~~~

In your webapplication descriptor web.xml, just add these lines:

.. code-block:: xml

	<filter>
		<!-- The name is important (thanks to Spring mecanisms). It's the filter bean name -->
		<filter-name>OAuth2Filter</filter-name>
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>OAuth2Filter</filter-name>
		
		<!-- Here the path of your protected resource -->
		<url-pattern>/yourProtectedPath</url-pattern>
	</filter-mapping>

The filter is a Spring bean, so you'll need to launch Spring with our servlet container (this is already the case if your Resource Server is a RESThub server).

The Servlet filters process OAuth2 protocol concerns, and delegates the token validation to another Spring beans.

Validation Service bean
~~~~~~~~~~~~~~~~~~~~~~~

We provides two validation services:
* org.resthub.oauth2.filter.service.ExternalValidationService: each incoming token are checked, by issuing a request to the Authorization Service.
* org.resthub.oauth2.filter.service.CachedExternalValidationService: subclass of the first one which cached (in memory) tokens during their lifetime, to avoid further calls for already known tokens.

In a Spring configuration file, past these lines :

.. code-block:: xml
	
	<!-- Scan Spring IOC annotations for oauth2 RESThub beans -->
	<context:component-scan base-package="org.resthub.oauth2.filter" />
	
	<!-- Declaration of the filter -->
	<bean name="OAuth2Filter" class="org.resthub.oauth2.filter.front.OAuth2Filter">
		<property name="service" ref="validationService" />
		<property name="resource" value="#{securityConfig.resourceName}" />
	</bean>
	
	<!-- Spring bean used by the filter to validate incoming tokens -->
	<bean name="validationService" init-method="postInit" class="org.resthub.oauth2.filter.service.CachedExternalValidationService">
		<property name="accessTokenParam" value="#{securityConfig.accessTokenParam}"/>
		<property name="tokenInformationEndpoint" value="#{securityConfig.tokenInformationEndpoint}"/>
		<property name="authorizationPassword" value="#{securityConfig.authorizationPassword}"/>
		
		<!-- Just for CachedExternalValidationService -->		
		<property name="discardPeriod" value="#{securityConfig.discardPeriod}"/>
	</bean>
	
	<!-- Security configuration -->
	<util:properties id="securityConfig">
		<!-- Your resource service name, for error message -->
		<prop key="resourceName">BusinessService</prop>
		
		<!-- ExternalValidationService: Your resource service name, for error message -->
		<prop key="accessTokenParam">access_token</prop>
		
		<!-- ExternalValidationService: Paste here the ip/domain of your Authorization service -->
		<prop key="tokenInformationEndpoint">https://XXX.XXX.XXX.XXX:PPPP/YYYYY/authorize/tokenDetails</prop>
			
		<!-- ExternalValidationService: The password used to request token information on Authorization service. -->
		<prop key="authorizationPassword">p@ssw0rd</prop>
		
		<!-- CachedExternalValidationService: tokens are cache during their lifetime + this discard period (in seconds). Negative values are allowed. -->
		<prop key="discardPeriod">5</prop>
	</util:properties>

From here, each request of your resource will be filtered, and if they contains valid tokens, processed.
Request without valid token will be rejected:
* 400 BAD REQUEST for request without tokens
* 401 UNAUTHORIZED for request with expired or invalid tokens
* 403 FORBIDDEN for token that do not have enought right.

You are free to provide your own validation mecanism (with cryptographic checks for example).\\
Just implement the ValidationService interface in a spring bean, and declare it as "validationService" bean.

Fine-grained permissions
~~~~~~~~~~~~~~~~~~~~~~~~

On your Authorization Server, the RESThub Authorization Service ask your specific Authentication Service for user permissions.\\
These permissions are returned to the Resource Server when processing an incoming request.\\
But what are thy used for ?

The servlet filter enrich the incoming HTTP request with these permissions (which are juste strings).
We can therefore used them with JSR-250 security annotations, directly in Jersey front-beans.

For example::

	@Path("/business")
	@Named("businessController")
	@Singleton
	public class BusinessController {
			
		@GET
		@Path("admin")
		@RolesAllowed({"ADMIN", "USER"})
		@Produces(MediaType.APPLICATION_JSON)
		public String helloWorldAdmin() {
		
		}
	}

The helloWorldAdmin() will be accessible only if the token is associated with the "ADMIN" or "USER" strings. If not, an HTTP status error 403 FORBIDDEN will be returned.

You'll need to enable the JSR-250 management in Jersey, by putting this in your web.xml, when declaring the JerseySpring servlet:

.. code-block:: xml

	<servlet>
    	<servlet-name>jersey</servlet-name>
    	<servlet-class>com.sun.jersey.spi.spring.container.servlet.SpringServlet</servlet-class>
    	<!-- Add the followng lines -->
    	<init-param>
    	    		<param-name>com.sun.jersey.spi.container.ResourceFilters</param-name>
    	    		<param-value>com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory</param-value>
 		</init-param>
 	</servlet>

As a bonus, you can access the user id related to the incoming request, the same that was returned by the provider AuthenticationService.getUser() method.
Two way are usable::

	@GET
	@Path("admin")
	@RolesAllowed({"ADMIN", "USER"})
	public String helloWorldAdmin(@HeaderParam("user_id") String userId) {
	
	}

or::

	@GET
	@Path("admin")
	@RolesAllowed({"ADMIN", "USER"})
	public String helloWorldAdmin(@Context HttpServletRequest req) {
		String userId = req.getUserPrincipal().getName();
	}

Currently, en incompatibilty between Spring-Security 3 and Jersey prevent us to populate the retrieved permissions in the Spring-Security's Principal object.

Client side
-----------

We provide some functionnalities also on client side.

Javascript client
~~~~~~~~~~~~~~~~~

Within the RESThub Javascript stack, we add two jquery functions:

.. code-block:: javascript

	/**
	 * Sends a request to get the access token.
	 * An OAuth 2 "token request" is sent to the $.oauth2Conf.tokenEndPoint url.
	 * 
	 * The returned token (if successful) is given to the specified callback.
	 * You may stores this token, and passes it further to the oaut2Ajax() function,
	 * which performs an access to an OAuth 2 protected resource.
	 * 
	 * @param username The resource owner login (end-user login).
	 * @param password The resource owner password (end-user password).
	 * @param success A callback, called when the token is returned by the server.
	 * This function takes only one parameter, which is the token (JSON structure).
	 * @param error A callback, called when the server refused to issue a token.
	 * This function takes two parameters: the first is the error string, and the second
	 * an option explanation.
	 */
	getOauth2token: function( username, password, success, error ) {					
	
	/**
	 * Sends an Ajax request to access an OAuth 2 protected resource.
	 * Uses the same first parameter as jquery.ajax() (which is internally called).
	 * It adds to this structure the attribute: 
	 * - authorizationError: function ( errorObj, XMLHttpRequest ) 
	 * This callback is called when an OAuth 2 protocol error occurs.
	 * errorObj is a structure containing :
	 * - status: A string returned by the server explaining error.
	 * - message: An optionnal explanation message.
	 * 
	 * @param params The jquery.ajax() parameter, containing url, data, callbacks...
	 * @param accessToken The accessToken structure, retrieved with getOauth2token().
	 */
	oauth2Ajax: function( params, accessToken) {

You'll have to set $.oauth2Conf.tokenEndPoint to the token endpoint of your Authorization Server.
Before accessing a protected Resource server, gather the end-user credentials (username and password), and get a token with $.getOauth2token();

Then, once the token returned (in parameter of the success callback), just use it as second argument of $.oauth2Ajax().
That's all that simple.

For those you are using the ResthubController jquery abstract class, just call this._authenticate() (same parameters as $.getOauth2token().\\\\
The retrieved token will be stored in HTML5 session, and furtherly used in this._securedPost(), this._securedGet(), this._securedDelete()... and this._securedAjax() calls. 

Java side
~~~~~~~~~

You will certainly have communications between protected resource services.
If you choose to use tokens between your protected service, as described in the (TODO) server-to-server profile, we provide you some utilities to do that.

In your "client" resource server:
# Add a maven dependency to org.resthub:resthub-oauth2-client.jar
# In your Dao/Service beans, use the TokenRepository utility class.

This class stores in memory tokens you may need to access as many protected resource as you want.
You have to configure it:
* By indicating some Authorization servers urls : just a list of string passed to setAuthenticationServices() (you can also use Spring injection by declaring a bean in xml)
* By indicating a client id and client secret. Thoses "client credentials" are NOT end-user credentials, and must be known by your authorization services. TokenRepository will use them to authenticate and obtain token to the desired resource.

**WARNING** - For this first release, client id/secret ARE end-user credential, so you need a "Technical user" in your authorization service that will represent your java clients//**

You can use in many ways this utility class:
# Just with enrich(). When enrich() will be invoked, existing token will be used, or if no token are available, a token will be automatically asked.
# With obtain(), add() and enrich(). Manually gets your token with obtain(), keeps it with add(), and it will be used when enrich() will be invoked.
# With add(), and consult(). Sets your token manually with addToken(), and retrieves them furtherly with consult(). you will just use TokenRepository as an in-memory storage space, and you will have to enrich yourself your requests

Some example?
Spring bean definition

.. code-block:: xml

	<bean name="securedClient" class="org.resthub.oauth2.client.TokenRepository">
		<property name="clientId" value="foo"/>
		<property name="clientSecret" value="bar"/>
		<property name="authorizationEndPoints">
			<list>
				<value>http://XXX.XXX.XXX.XXX:YYY/authorizationServer/authorize</value>
			</list>
		</property>
	</bean>

Java code::

	@Inject
	protected TokenRepository securedClient;
	
	// Enrich and trigger a request.
	String resourceName = "/myResource";
	XXX result = tested.enrich(httpClient.path(resourceName)).get(XXX.class);
