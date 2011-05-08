package org.resthub.oauth2.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.DispatcherType;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.web.jackson.JacksonProvider;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * Tests the the TokenFactory utility class.
 * Launches an in-memory jetty server with two contexts:
 * <ol><li>an authorization service build upon resthub-oauth2-provider.</li>
 * <li>a resource service protected with resthub-oauth2-filter.</li></ol>
 */
public class TestTokenRepository {

	// -----------------------------------------------------------------------------------------------------------------
	// Static private attributes
	
	public static final String CLIENT_ID = "test";
	public static final String CLIENT_SECRET = "";
	
	/**
	 * Tested utility.
	 */
	protected static TokenRepository tested;

	/**
	 * Jetty memory server instance.
	 */
	protected static Server server;
	
	/**
	 * Jetty memory server port.
	 */
	protected static int port = 9797;
 
	// -----------------------------------------------------------------------------------------------------------------
	// Test suite initialization and finalization

	/**
	 * Before the test suite, launches a Jetty inmemory server.
	 */
	@BeforeClass
	public static void suiteSetUp() throws Exception {
		// Creates a Jetty server.
		server = new Server(port);
		
		// Add a context for authorization service
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setDisplayName("resthub test webapp");
	
		context.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml classpath*:applicationContext.xml");
		FilterHolder filterDef = new FilterHolder(DelegatingFilterProxy.class);
        filterDef.setName("springSecurityFilterChain");
		context.addFilter(filterDef, "/*", 0);	
		context.addEventListener(new ContextLoaderListener());
		context.addServlet(SpringServlet.class, "/api/*");

		// Starts the server.
        server.setHandler(context);
			
		server.start();
		
		// Tested service initialization.
		tested = new TokenRepository();
		tested.setClientId(CLIENT_ID);
		tested.setClientSecret(CLIENT_SECRET);
	} // suiteSetUp().

	/**
	 * After the test suite, stops the Jetty inmemory server.
	 */
	@AfterClass
	public static void suiteTearDown() throws Exception {
		if (server != null) {
			server.stop();
		}
	} // suiteTearDown().

	// -----------------------------------------------------------------------------------------------------------------
	// Tests

	/**
	 * Check the token obtention, and its several error cases.
	 */
	@Test
	public void testObtain() {
		
		// Initialize the TokenRepository
		String username = "test";
		String password = "t3st";
		String scope = "";
		
		List<String> endPoints = new ArrayList<String>();
		// TODO : [SDE] I don't interstand it is /api/oauth/authorize and not /oauth/authorize ...
		endPoints.add("http://localhost:"+port+"/api/oauth/authorize");
		tested.setAuthorizationEndPoints(endPoints);
		
		TokenResponse token = tested.obtain(username, password, scope);
		assertNotNull("No response returned by obtain()", token);
		assertNotNull("An access token was not generated", token.accessToken);
		
		// Wrong username
		try {
			tested.obtain("unknown", password, scope);
			fail("An NoTokenFoundException must be raised for token obtention with wrong clientId");
		} catch (NoTokenFoundException exc) {
			// Everything right.
		}
		
		// Wrong password.
		try {
			tested.obtain(username, "unknown", scope);
			fail("An NoTokenFoundException must be raised for token obtention with wrong clientPassword");
		} catch (NoTokenFoundException exc) {
			// Everything right.
		}
		
		// No providers.
		try {
			tested.setAuthorizationEndPoints(new ArrayList<String>());
			tested.obtain(username, password, scope);
			fail("An NoTokenFoundException must be raised for token obtention with no providers");
		} catch (NoTokenFoundException exc) {
			// Everything right.
		}
	} // testObtain.

	/**
	 * Tests the addition of tokens in the repository.
	 */
	@Test
	public void testAddTokenConsult() {
		String resource = "/myResource";

		// For now, no tokens known
		List<TokenResponse> known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("No token might be known for new resource", 0, known.size());
		
		TokenResponse t1 = new TokenResponse();
		t1.accessToken = "aaaa";
		t1.scope = "read";
		
		// Just adds a token.
		tested.addToken(resource, t1);
		known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 1, known.size());
		assertTrue("The token 1 is not in known list", known.contains(t1));
	
		TokenResponse t2 = new TokenResponse();
		t2.accessToken = "bbbb";
		t2.scope = "write";
		
		// Just adds another token.
		tested.addToken(resource, t2);
		known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 2, known.size());
		assertTrue("The token 1 is not in known list", known.contains(t1));
		assertTrue("The token 2 is not in known list", known.contains(t2));

		TokenResponse t3 = new TokenResponse();
		t3.accessToken = "cccc";
		t3.scope = "read";
		
		// Adds another token with same scope.
		tested.addToken(resource, t3);
		known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 2, known.size());
		assertTrue("The token 3 is not in known list", known.contains(t3));
		assertTrue("The token 2 is not in known list", known.contains(t2));
		assertFalse("The token 1 still in known list", known.contains(t1));
		
		// Adds a token on another resource.
		String resource2 = "/myOtherResource";
		tested.addToken(resource2, t3);
		known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 2, known.size());
		assertTrue("The token 3 is not in known list", known.contains(t3));
		assertTrue("The token 2 is not in known list", known.contains(t2));

		known = tested.consult(resource2);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 1, known.size());
		assertTrue("The token 3 is not in known list", known.contains(t3));
	} // testAddTokenConsult().

	/**
	 * Test the request auto enrichment.
	 */
	@Test
	public void testAutoEnrich() {
		ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(new JacksonProvider());
		String resource = "/api/resource/hello";
		WebResource httpClient = Client.create(config).resource("http://localhost:"+port);
		
		// Check we cannot access the protected resource.
		try {
			httpClient.path(resource).get(String.class);
			fail("The protected resource is accessible without any token!");
		} catch (UniformInterfaceException exc) {
			assertEquals("The HTTP error code received is not correct", 401, exc.getResponse().getStatus());
		}
		
		// Initialize the TokenRepository
		tested.addCredentials("/api", "test", "t3st");
		List<String> endPoints = new ArrayList<String>();
		// TODO : [SDE] I don't interstand it is /api/oauth/authorize and not /oauth/authorize ...
		endPoints.add("http://localhost:"+port+"/api/oauth/authorize");
		tested.setAuthorizationEndPoints(endPoints);
		
		// Enrich and trigger a request.
		String result = tested.enrich(httpClient.path(resource)).get(String.class);
		assertEquals("Result returned is unexpected", "Hello", result);	
	} // testEnrich().

	/**
	 * Test the request enrichment, with manuall token obtention and retention.
	 */
	@Test
	public void testEnrich() {
		ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(new JacksonProvider());
		String resource = "/api/resource/hello";
		WebResource httpClient = Client.create(config).resource("http://localhost:"+port);
		
		// Check we cannot access the protected resource.
		try {
			httpClient.path(resource).get(String.class);
			fail("The protected resource is accessible without any token!");
		} catch (UniformInterfaceException exc) {
			assertEquals("The HTTP error code received is not correct", 401, exc.getResponse().getStatus());
		}
		
		// Initialize the TokenRepository
		tested.addCredentials("/api", "test", "t3st");
		List<String> endPoints = new ArrayList<String>();
		// TODO : [SDE] I don't interstand it is /api/oauth/authorize and not /oauth/authorize ...
		endPoints.add("http://localhost:"+port+"/api/oauth/authorize");
		tested.setAuthorizationEndPoints(endPoints);
		
		// Enrich a request to an unknown resource
		try {
			tested.enrich(httpClient.path("2"+resource)).get(String.class);
			fail("An resource for whom no credentials are specified must not be accessible");
		} catch (NoTokenFoundException exc) {
			// No token, right.
		}
		
		// Enrich a request to an unknown resource but with credentials
		try {
			tested.enrich(httpClient.path(resource+"/2")).get(String.class);
			fail("An unknown resource must not be accessible");
		} catch (UniformInterfaceException exc) {
			assertEquals("The HTTP error code received is not correct", 404, exc.getResponse().getStatus());
		}

		String scope = "";
		TokenResponse token = tested.obtain("test", "t3st", scope);
		
		// Adds the return token in the token repository
		tested.addToken(resource, token);
		// Enrich and trigger a request.
		String result = tested.enrich(httpClient.path(resource)).get(String.class);
		assertEquals("Result returned is unexpected", "Hello", result);

		// TODO test scopes.
		
	} // testEnrich().

} // Class TestTokenRepository
