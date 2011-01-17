package org.resthub.oauth2.provider.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.Status;

import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.oauth2.common.exception.ProtocolException.Error;
import org.resthub.oauth2.common.front.model.ObtainTokenErrorResponse;
import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.oauth2.common.model.Token;
import org.resthub.oauth2.provider.service.MockAuthenticationService;
import org.resthub.web.jackson.JacksonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * Test of the front layer, from an Http client point of view.
 */
public class AuthorizationControllerTest {
	
	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes
	
	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * In-memory Jetty port
	 */
	protected static int port = 9797;

	/**
	 * In-memory server
	 */
    protected static Server server;

    /**
     * Spring context configuration.
     */
    protected static String contextLocations = "classpath*:resthubContext.xml classpath:resthubContext.xml classpath:applicationContext.xml";

	// -----------------------------------------------------------------------------------------------------------------
	// Constants

	/**
	 * Known client Id used by mocks
	 */
	public static final String CLIENT_ID = "";
	
	/**
	 * Unknown client Id used by mocks
	 */
	public static final String UNKNOWN_CLIENT_ID = "unknownClientId";

	/**
	 * Good redirection uri used by mocks
	 */
	public static final String REDIRECT_URI = "http://localhost:" + port + "/redirect";

	// -----------------------------------------------------------------------------------------------------------------
	// Tests initialization/finalization.

    @BeforeClass
    public static void suiteSetUp() throws Exception {
        server = new Server(port);

        // On lance le serveur Jetty
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.setDisplayName("resthub test webapp");
        context.setContextPath("/");

        context.getInitParams().put("contextConfigLocation", contextLocations);
        context.setBaseResource(Resource.newResource("src/main/webapp"));
        context.addFilter(OpenEntityManagerInViewFilter.class, "/*", 1);
        context.addServlet(JspServlet.class, "/jsp/*");
        context.addServlet(SpringServlet.class, "/api/*");
        context.addEventListener(new ContextLoaderListener());
        server.setHandler(context);
        server.start();
    } // suiteSetUp().

    @AfterClass
    public static void suiteTearDown() throws Exception {
        if (server != null) {
            server.stop();
        }
    } // suiteTearDown().

	// -----------------------------------------------------------------------------------------------------------------
	// Private methods

    /**
	 * Creates a client that does not follow redirections 
	 */
	 protected WebResource resource() {
        ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(new JacksonProvider());
        Client client = Client.create(config);
        return client.resource("http://localhost:" + port+"/api");
    }

	/**
	 * Creates a client that does not follow redirections 
	 */
	protected WebResource notFollowingResource() {
        ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(new JacksonProvider());
        Client client = Client.create(config);
		client.setFollowRedirects(false);
        return client.resource("http://localhost:" + port+"/api");
	} // notFollowingResource().
	
	/**
	 * Asserts that the response is a redirection, and contains error code
	 * 
	 * @param response The tested response.
	 * @param error the awaited error code.
	 */
	protected void assertRedirectFailed(ClientResponse response, String error) {
		assertEquals("Response not redirected", 302, response.getStatus());
		assertTrue("No redirection url in response", response.getHeaders().containsKey(HttpHeaders.LOCATION));		
		String redirect = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		assertTrue("No error parameter or wrong value: "+redirect, redirect.contains("error=" +error));
	} // assertRedirectFailed().
	
	// -----------------------------------------------------------------------------------------------------------------
	// Tests

	/**
	 * Test the protocol implementation of the token obtention. 
	 */
	@Test
	public void obtainAndRetrieveToken() {
		logger.info("[obtainAndRetrieveToken] ask for a token");
		WebResource server = resource();
		
		Form form = new Form();
		form.add("grant_type", "password");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("username", "test");
		form.add("password", "t3st");

		// gets a token
		ClientResponse response = server.path("authorize/token").
			type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
		TokenResponse tokenResponse = response.getEntity(TokenResponse.class);
		
		logger.info("[obtainAndRetrieveToken] generated token: {}", tokenResponse);
		assertNotNull("Generated token is null", tokenResponse);
		assertNotNull("Generated access token is null", tokenResponse.accessToken);
		assertNotNull("Token doesn't have expire date", tokenResponse.expiresIn);
		assertNotNull("Generated refresh token is null", tokenResponse.refreshToken);
		assertNull("Token must not have scope", tokenResponse.scope);
		NewCookie awaited = new NewCookie("oauth_token", tokenResponse.accessToken, "/", "", "", 
				tokenResponse.expiresIn, false);
		assertTrue("Cookie was not set", response.getCookies().contains(awaited));

		// Retrieves informations
		Token token = server.path("authorize/tokenDetails").queryParam("access_token", tokenResponse.accessToken).
				header(HttpHeaders.AUTHORIZATION, "p@ssw0rd").get(Token.class);
		
		logger.info("[obtainAndRetrieveToken] retrieved token: {}", token);
		assertNotNull("Retreived token is null", token);
		assertNotNull("Retreived access token is null", token.accessToken);
		assertNotNull("Token doesn't have expire date", token.lifeTime);
		assertNotNull("Generated refresh token is null", token.refreshToken);
		assertNotNull("Token creation date is null", token.createdOn);
		assertNotNull("Token's permissions are null", token.permissions);
		assertTrue("Admin right was lost", token.permissions.contains(MockAuthenticationService.ADMIN_RIGHT));
		assertTrue("User right was lost", token.permissions.contains(MockAuthenticationService.USER_RIGHT));
		assertNotNull("Token's user identifier is null", token.userId);
		
		// Compares to original response.
		assertEquals("Retrieved token has not good access token", tokenResponse.accessToken, token.accessToken);
		assertEquals("Retrieved token has not good refresh token", tokenResponse.refreshToken, token.refreshToken);
		assertEquals("Retrieved token has not good lifetime", tokenResponse.expiresIn, token.lifeTime);

		// gets another token with no permissions
		form = new Form();
		form.add("grant_type", "password");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("username", MockAuthenticationService.NO_PERMISSIONS_USERNAME);
		form.add("password", "t3st");
		response = server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, 
				form);
		tokenResponse = response.getEntity(TokenResponse.class);
	
		logger.info("[obtainAndRetrieveToken] generated token: {}", tokenResponse);
		assertNotNull("Generated token is null", tokenResponse);
		assertNotNull("Generated access token is null", tokenResponse.accessToken);
		assertNotNull("Token doesn't have expire date", tokenResponse.expiresIn);
		assertNotNull("Generated refresh token is null", tokenResponse.refreshToken);
		assertNull("Token must not have scope", tokenResponse.scope);
		awaited = new NewCookie("oauth_token", tokenResponse.accessToken, "/", "", "", 
				tokenResponse.expiresIn, false);
		assertTrue("Cookie was not set", response.getCookies().contains(awaited));
		
		// Retrieves informations
		token = server.path("authorize/tokenDetails").queryParam("access_token", tokenResponse.accessToken).
				header(HttpHeaders.AUTHORIZATION, "p@ssw0rd").get(Token.class);
		
		logger.info("[obtainAndRetrieveToken] retrieved token: {}", token);
		assertNotNull("Retreived token is null", token);
		assertNotNull("Retreived access token is null", token.accessToken);
		assertNotNull("Token doesn't have expire date", token.lifeTime);
		assertNotNull("Generated refresh token is null", token.refreshToken);
		assertNotNull("Token creation date is null", token.createdOn);
		assertNotNull("Token's permissions are null", token.permissions);
		assertFalse("Admin right was returned", token.permissions.contains(MockAuthenticationService.ADMIN_RIGHT));
		assertFalse("User right was returned", token.permissions.contains(MockAuthenticationService.USER_RIGHT));
		assertNotNull("Token's user identifier is null", token.userId);
		
		// Compares to original response.
		assertEquals("Retrieved token has not good access token", tokenResponse.accessToken, token.accessToken);
		assertEquals("Retrieved token has not good refresh token", tokenResponse.refreshToken, token.refreshToken);
		assertEquals("Retrieved token has not good lifetime", tokenResponse.expiresIn, token.lifeTime);

	} // obtainAndRetrieveToken().

	/**
	 * Test error cases for token retrieval.
	 */
	@Test
	public void obtainTokenDetailsErrorCase() {
		WebResource server = resource();
		
		try {
			logger.info("[obtainTokenDetailsErrorCase] no password");
			server.path("authorize/tokenDetails").get(Token.class);
			fail("An UniformInterfaceException must be raised for missing access_type");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 403, exc.getResponse().getStatus());
		}

		try {
			logger.info("[obtainTokenDetailsErrorCase] no access_type");
			server.path("authorize/tokenDetails").header(HttpHeaders.AUTHORIZATION, "p@ssw0rd").get(Token.class);
			fail("An UniformInterfaceException must be raised for missing access_type");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 500, exc.getResponse().getStatus());
		}
	} // obtainTokenDetailsErrorCase().

	/**
	 * Test error cases for token obtention.
	 */
	@Test
	public void obtainTokenErrorCase() {
		WebResource server = resource();
		Form form;

		try {
			logger.info("[obtainToken] no grant_type");
			form = new Form();
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing grant_type");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_REQUEST.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}
		
		try {
			logger.info("[obtainToken] unsupported grant_type");
			form = new Form();
			form.add("grant_type", "authorization-code");
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for unsupported grant_type");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.UNSUPPORTED_GRANT_TYPE.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] missing client_id");
			form = new Form();
			form.add("grant_type", "password");
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing client_id");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_CLIENT.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] invalid client_id");
			form = new Form();
			form.add("grant_type", "password");
			form.add("client_id", "unknown");
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid client_id");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_CLIENT.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] missing client_secret");
			form = new Form();
			form.add("grant_type", "password");
			form.add("client_id", null);
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing client_secret");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_CLIENT.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] invalid client_secret");
			form = new Form();
			form.add("grant_type", "password");
			form.add("client_id", null);
			form.add("client_secret", "unknown");
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid client_secret");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_CLIENT.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] missing username");
			form = new Form();
			form.add("grant_type", "password");
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("password", "t3st");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing username");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_REQUEST.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] missing password");
			form = new Form();
			form.add("grant_type", "password");
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("username", "test");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing password");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_REQUEST.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] invalid scope");
			form = new Form();
			form.add("grant_type", "password");
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			form.add("scope", "123 5435, aDIF");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid scope");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_SCOPE.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] unknown scope");
			form = new Form();
			form.add("grant_type", "password");
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			form.add("scope", "123 5435 aDIF");
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for unknown scope");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_SCOPE.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

	} // obtainToken().
	
	@Test
	public void shouldResponseTypeBeRequiredToGetAccessCode() {
		// When accessing the end-user authorization end-point without response_type
		ClientResponse response = notFollowingResource().path("authorize")
					.queryParam("client_id", CLIENT_ID)
					.queryParam("redirect_uri", REDIRECT_URI)
					.get(ClientResponse.class);
		
		// Then an invalid_request error is returned
		assertRedirectFailed(response, "invalid_request");
	} // shouldResponseTypeBeRequiredToGetAccessCode().

	@Test
	public void shouldWrongResponseTypeFailOnError() {
		// When accessing the end-user authorization end-point with wrong response_type
		ClientResponse response = notFollowingResource().path("authorize")
				.queryParam("client_id", CLIENT_ID)
				.queryParam("response_type", "unknown")
				.queryParam("redirect_uri", REDIRECT_URI)
				.get(ClientResponse.class);
		
		// Then an unsupported_response_type error is returned
		assertRedirectFailed(response, "unsupported_response_type");	
	} // shouldWrongResponseTypeFailOnError().

	@Test
	public void shouldClientIdBeRequiredToGetAccessCode() {
		// When accessing the end-user authorization end-point without client_id
		ClientResponse response = notFollowingResource().path("authorize")
				.queryParam("response_type", "code")
				.queryParam("redirect_uri", REDIRECT_URI)
				.get(ClientResponse.class);

		// Then an invalid_request error is returned
		assertRedirectFailed(response, "invalid_request");			
	} // shouldClientIdBeRequiredToGetAccessCode().

	@Test
	public void shouldUnknownClientIdFailOnError() {
		// When accessing the end-user authorization end-point with unknown client_id
		ClientResponse response = notFollowingResource().path("authorize")
				.queryParam("client_id", UNKNOWN_CLIENT_ID)
				.queryParam("response_type", "code")
				.queryParam("redirect_uri", REDIRECT_URI)
				.get(ClientResponse.class);

		// Then an invalid_client error is returned
		assertRedirectFailed(response, "invalid_client");			
	} // shouldUnknownClientIdFailOnError().

	@Test
	public void shouldRedirectUriBeRequiredToGetAccessCode() {
		try {
			// When accessing the end-user authorization end-point without redirect_uri
			notFollowingResource().path("authorize")
					.queryParam("client_id", CLIENT_ID)
					.queryParam("response_type", "code")
					.get(ClientResponse.class);
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Then an invalid_request error is returned
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_REQUEST, response.error);
			logger.info("[shouldMisformatedRedirectUriFailOnError] response returned: {}", response);
		}		
	} // shouldRedirectUriBeRequiredToGetAccessCode().

	@Test
	public void shouldMisformatedRedirectUriFailOnError() {
		try {
			// When accessing the end-user authorization end-point with invalid redirect_uri
			notFollowingResource().path("authorize")
					.queryParam("client_id", CLIENT_ID)
					.queryParam("response_type", "code")
					.queryParam("redirect_uri", "://redirected")
					.get(ClientResponse.class);
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Then an invalid_request error is returned
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_REQUEST, response.error);
			logger.info("[shouldMisformatedRedirectUriFailOnError] response returned: {}", response);
		}
	} // shouldMisformatedRedirectUriFailOnError().

	@Test
	public void shouldEndUserAuthorizationEndPointBeDisplayed() {
		// When accessing the end-user authorization end-point
		ClientResponse response = resource().path("authorize")
			.queryParam("client_id", CLIENT_ID)
			.queryParam("response_type", "code")
			.queryParam("redirect_uri", REDIRECT_URI)
			.get(ClientResponse.class);
		
		// Then the end-point is accessible
		assertEquals("Page not displayed", Status.OK.getStatusCode(), response.getStatus());
	} // shouldEndUserAuthorizationendPointBeDisplayed().

	@Test
	public void shouldUnknwonEndUserBeRedirectedWithError() {
		// Given a known user and a good redirection URI
		Form form = new Form();
		form.add("redirect_uri", REDIRECT_URI);
		form.add("username", MockAuthenticationService.UNKNOWN_USERNAME);
		form.add("password", "t3st");
		
		// When accessing the end-user authorization end-point
		ClientResponse response = notFollowingResource().path("authorize/authenticate").post(ClientResponse.class, form);
				
		// Then an error code is added to the redirection uri
		assertRedirectFailed(response, "access_denied");			
	} // shouldEndUserAuthenticateAndRedirectedWithCode().

	@Test
	public void shouldEndUserAuthenticateAndRedirectedWithCode() {
		// Given a known user and a good redirection URI
		Form form = new Form();
		form.add("redirect_uri", REDIRECT_URI);
		form.add("username", "test");
		form.add("password", "t3st");

		// When accessing the end-user authorization end-point
		ClientResponse response = notFollowingResource().path("authorize/authenticate").post(ClientResponse.class, form);
		
		// Then the user-agent is redirected to the redirect_uri
		assertEquals("Response not redirected", 302, response.getStatus());
		assertTrue("No redirection url in response", response.getHeaders().containsKey(HttpHeaders.LOCATION));		
		String redirect = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		assertTrue("Wrong redirection: "+redirect, redirect.startsWith(REDIRECT_URI));
		
		// Then an access_code is added to the redirection uri
		assertTrue("No access_code parameter or wrong value: "+redirect, redirect.contains("code="));
	} // shouldEndUserAuthenticateAndRedirectedWithCode().

	@Test
	public void shouldGetTokenWithAccessCode() {
		// Given an access code and redirect URI.
		Form form = new Form();
		form.add("redirect_uri", REDIRECT_URI);
		form.add("username", "test");
		form.add("password", "t3st");
		ClientResponse response = notFollowingResource().path("authorize/authenticate").post(ClientResponse.class, form);
		String redirect = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		String accessCode = redirect.replace(REDIRECT_URI, "").replaceAll("&", "").replace("state=", "").
		replace("code=", "");

		// When accessing the token endpoint with access code
		WebResource server = resource();		
		form = new Form();
		form.add("grant_type", "authorization_code");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("code", accessCode);
		form.add("redirect_uri", REDIRECT_URI);
		TokenResponse tokenResponse = server.path("authorize/token").
			type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
		
		// Then a token is returned.
		assertNotNull("Generated token is null", tokenResponse);
		assertNotNull("Generated access token is null", tokenResponse.accessToken);		
	} // shouldGetTokenWithAccessCode().

	@Test
	public void shouldInvalidAccessCodeFailonError() {
		// Given an unknown access code and redirect URI.		
		WebResource server = resource();		
		Form form = new Form();
		form.add("grant_type", "authorization_code");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("code", "123456");
		form.add("redirect_uri", REDIRECT_URI);
		try {
			// When accessing the token endpoint with access code
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid code");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Then an error is returned
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_REQUEST.value(), response.error);
		}
	} // shouldInvalidAccessCodeFailonError().

	@Test
	public void shouldAccessCodeBeRequired() {
		// Given a redirect URI and without access_code.		
		WebResource server = resource();		
		Form form = new Form();
		form.add("grant_type", "authorization_code");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("redirect_uri", REDIRECT_URI);
		try {
			// When accessing the token endpoint without access code on grant_type authorization_code
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid code");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Then an error is returned
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_REQUEST.value(), response.error);
		}
	} // shouldAccessCodeBeRequired().

	@Test
	public void shouldInvalidRedirectURIFailonError() {
		// Given an access_code and whitout redirect URI.		
		Form form = new Form();
		form.add("redirect_uri", REDIRECT_URI);
		form.add("username", "test");
		form.add("password", "t3st");
		ClientResponse response = notFollowingResource().path("authorize/authenticate").post(ClientResponse.class, form);
		String redirect = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		String accessCode = redirect.replace(REDIRECT_URI, "").replaceAll("&", "").replace("state=", "").
				replace("code=", "");

		WebResource server = resource();		
		form = new Form();
		form.add("grant_type", "authorization_code");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("redirect_uri", "://redirected");
		form.add("code", "123456");
		try {
			// When accessing the token endpoint without redirect URIe on grant_type authorization_code
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid code");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Then an error is returned
			ObtainTokenErrorResponse error = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_REQUEST.value(), error.error);
		}
	} // shouldInvalidRedirectURIFailonError().

	@Test
	public void shouldRedirectURIBeRequired() {
		// Given an access_code and whitout redirect URI.		
		Form form = new Form();
		form.add("redirect_uri", REDIRECT_URI);
		form.add("username", "test");
		form.add("password", "t3st");
		ClientResponse response = notFollowingResource().path("authorize/authenticate").post(ClientResponse.class, form);
		String redirect = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		String accessCode = redirect.replace(REDIRECT_URI, "").replaceAll("&", "").replace("state=", "").
				replace("code=", "");

		WebResource server = resource();		
		form = new Form();
		form.add("grant_type", "authorization_code");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("code", "123456");
		try {
			// When accessing the token endpoint without redirect URIe on grant_type authorization_code
			server.path("authorize/token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid code");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Then an error is returned
			ObtainTokenErrorResponse error = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Error.INVALID_REQUEST.value(), error.error);
		}
	} // shouldRedirectURIBeRequired().

	// TODO refresh token.
} // class AuthorizationControllerTest
