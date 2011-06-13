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

* Resource owner: Something (someone) who can grant access to a business object (a resource). The end-user is the resource owner over all it's related business objects.
* Resource server: The REST Service which hosts a business object (a resource).
* Client: Server, browser, mobile application or standalone application which want to get access to a resource.
* Authorization server: The REST Service which can decide if a client has access to a resource on behalf of a Resource owner.

AAA on RESThub
==============

Principles
----------

What do we need?

* A secured and efficient solution: credentials will only be exchanged once, and never again. Access on a resource server is secured and the security checks do not heavily impact runtime performances.
* An unic-overall access: users will be grant access on the whole distributed application.
* A fine-grained authorization: each Resource server has to maintain its own permissions
* A centralized and specialized accounting: we need to plug the accounting part on your own business users management service.

Summary
-------

We can resume all by a diagram:

.. image:: _static/oauth-1.png

Concretly
---------

We will thus provide:

* A java authorization server layer, which you have to plug ahead your centralized accounting service.
* A java servlet resource server filter, which must be deployed on each services
* A javascript client toolkit, to performs authorization request, stores tokens, enrich further calls with those tokens, and manage security protocol errors.

In a chronologic order:

* the javascript toolkit will helps you perform the authorization request, with your user credentials.
* the autorization server layer will delegate to our accounting service the authentication part.
* once the user authenticated, the authorization server layer will returned a time-limited token
* the javascript toolkit will stores this token, and enrich your further calls to the REST services.
* on each REST services, the resource server filter will check the token validity, by requesting the central accounting service.
* the autorization server layer will delegate to out accounting service the retrieving of the user permissions, related to the incoming token. For further calls, results will be stored in a Memcache's distributed  cache.
* the resource server filter will enrich a spring security "Principal" object with the retrieved permission, allowing you to managed our business security.
* you can access to the authenticated user id, stored in a request header "user_id"

Usage
=====

OAuth2 provider
---------------

RESThub use `Spring Security OAuth2 implementation <http://static.springsource.org/spring-security/oauth/oauth2.html>`_ on serverside.
You can have look to Booking or Identity manager to see how it works.

OAuth2 client
-------------

RESThub JS provides OAuth2 support on client side.

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

* Add a maven dependency to org.resthub:resthub-oauth2-client.jar
* In your Dao/Service beans, use the TokenRepository utility class.

This class stores in memory tokens you may need to access as many protected resource as you want.
You have to configure it:

* By indicating some Authorization servers urls : just a list of string passed to setAuthenticationServices() (you can also use Spring injection by declaring a bean in xml)
* By indicating a client id and client secret. Thoses "client credentials" are NOT end-user credentials, and must be known by your authorization services. TokenRepository will use them to authenticate and obtain token to the desired resource.

**WARNING** - For this first release, client id/secret ARE end-user credential, so you need a "Technical user" in your authorization service that will represent your java clients//**

You can use in many ways this utility class:

* Just with enrich(). When enrich() will be invoked, existing token will be used, or if no token are available, a token will be automatically asked.
* With obtain(), add() and enrich(). Manually gets your token with obtain(), keeps it with add(), and it will be used when enrich() will be invoked.
* With add(), and consult(). Sets your token manually with addToken(), and retrieves them furtherly with consult(). you will just use TokenRepository as an in-memory storage space, and you will have to enrich yourself your requests

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
