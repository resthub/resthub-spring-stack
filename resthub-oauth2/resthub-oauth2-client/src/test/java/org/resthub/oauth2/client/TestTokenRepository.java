package org.resthub.oauth2.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.oauth2.test.authorizationService.MockAuthenticationService;
import org.resthub.oauth2.utils.JacksonProvider;
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
		ServletContextHandler authorization = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		authorization.setContextPath("/authorizationServer");			

		authorization.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml classpath:AuthorizationContext.xml");
		authorization.addServlet(SpringServlet.class, "/*");
		authorization.addEventListener(new ContextLoaderListener());
 
		// Add a context for resource service
		ServletContextHandler resource = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		resource.setContextPath("/resourceServer");		
		
		resource.getInitParams().put("contextConfigLocation", "classpath*:resthubContext.xml classpath:ResourceContext.xml");
		FilterHolder filterDef = new FilterHolder(DelegatingFilterProxy.class);
		filterDef.setName("oauth2filter");
		resource.addFilter(filterDef, "/*", 1);
		
		ServletHolder servletDef = new ServletHolder(SpringServlet.class);
		servletDef.setInitParameter("com.sun.jersey.spi.container.ResourceFilters", 
				"com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory");
		resource.addServlet(servletDef, "/*");
		resource.addEventListener(new ContextLoaderListener());

		// Starts the server.
		ContextHandlerCollection handlers = new ContextHandlerCollection();
        handlers.setHandlers(new Handler[] {authorization, resource});
        server.setHandler(handlers);
		server.start();
		
		// Tested service initialization.
		tested = new TokenRepository();
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
		tested.setClientId("any");
		tested.setClientSecret("any");
		List<String> endPoints = new ArrayList<String>();
		endPoints.add("http://localhost:"+port+"/authorizationServer/authorize");
		tested.setAuthenticationServices(endPoints);
		
		String resource = "/myResource";
		String scope = "";
		TokenResponse token = tested.obtain(resource, scope);
		assertNotNull("No response returned by obtain()", token);
		assertNotNull("An access token was not generated", token.accessToken);
		
		// Wrong client id obtention.
		try {
			tested.setClientId(MockAuthenticationService.UNKNOWN_CLIENT_ID);
			tested.obtain(resource, scope);
			fail("An NoTokenFoundException must be raised for token obtention with wrong clientId");
		} catch (NoTokenFoundException exc) {
			// Everything right.
		}
		
		// Wrong client id obtention.
		try {
			tested.setClientId("any");
			tested.setClientSecret(MockAuthenticationService.UNKNOWN_CLIENT_SECRET);
			tested.obtain(resource, scope);
			fail("An NoTokenFoundException must be raised for token obtention with wrong clientPassword");
		} catch (NoTokenFoundException exc) {
			// Everything right.
		}
		
		// No providers.
		try {
			tested.setAuthenticationServices(new ArrayList<String>());
			tested.obtain(resource, scope);
			fail("An NoTokenFoundException must be raised for token obtention with no providers");
		} catch (NoTokenFoundException exc) {
			// Everything right.
		}
	} // testObtain.

	/**
	 * Tests the addition of tokens in the repository.
	 */
	@Test
	public void testAddConsult() {
		String resource = "/myResource";

		// For now, no tokens known
		List<TokenResponse> known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("No token might be known for new resource", 0, known.size());
		
		TokenResponse t1 = new TokenResponse();
		t1.accessToken = "aaaa";
		t1.scope = "read";
		
		// Just adds a token.
		tested.add(resource, t1);
		known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 1, known.size());
		assertTrue("The token 1 is not in known list", known.contains(t1));
	
		TokenResponse t2 = new TokenResponse();
		t2.accessToken = "bbbb";
		t2.scope = "write";
		
		// Just adds another token.
		tested.add(resource, t2);
		known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 2, known.size());
		assertTrue("The token 1 is not in known list", known.contains(t1));
		assertTrue("The token 2 is not in known list", known.contains(t2));

		TokenResponse t3 = new TokenResponse();
		t3.accessToken = "cccc";
		t3.scope = "read";
		
		// Adds another token with same scope.
		tested.add(resource, t3);
		known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 2, known.size());
		assertTrue("The token 3 is not in known list", known.contains(t3));
		assertTrue("The token 2 is not in known list", known.contains(t2));
		assertFalse("The token 1 still in known list", known.contains(t1));
		
		// Adds a token on another resource.
		String resource2 = "/myOtherResource";
		tested.add(resource2, t3);
		known = tested.consult(resource);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 2, known.size());
		assertTrue("The token 3 is not in known list", known.contains(t3));
		assertTrue("The token 2 is not in known list", known.contains(t2));

		known = tested.consult(resource2);
		assertNotNull("Null token list are not allowed", known);
		assertEquals("At least one token may be known", 1, known.size());
		assertTrue("The token 3 is not in known list", known.contains(t3));
	} // testAddConsult().

	/**
	 * Test the request auto enrichment.
	 */
	@Test
	public void testAutoEnrich() {
		ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(new JacksonProvider());
		String resource = "/resourceServer";
		WebResource httpClient = Client.create(config).resource("http://localhost:"+port);
		
		// Check we cannot access the protected resource.
		try {
			httpClient.path(resource).get(String.class);
			fail("The protected resource is accessible without any token!");
		} catch (UniformInterfaceException exc) {
			assertEquals("The HTTP error code received is not correct", 401, exc.getResponse().getStatus());
		}
		
		// Initialize the TokenRepository
		tested.setClientId("any");
		tested.setClientSecret("any");
		List<String> endPoints = new ArrayList<String>();
		endPoints.add("http://localhost:"+port+"/authorizationServer/authorize");
		tested.setAuthenticationServices(endPoints);
		
		// Enrich and trigger a request.
		String result = tested.enrich(httpClient.path(resource)).get(String.class);
		assertEquals("Result returned is unexpected", "Hello World", result);	
	} // testEnrich().

	/**
	 * Test the request enrichment, with manuall token obtention and retention.
	 */
	@Test
	public void testEnrich() {
		ClientConfig config = new DefaultClientConfig();
        config.getSingletons().add(new JacksonProvider());
		String resource = "/resourceServer";
		WebResource httpClient = Client.create(config).resource("http://localhost:"+port);
		
		// Check we cannot access the protected resource.
		try {
			httpClient.path(resource).get(String.class);
			fail("The protected resource is accessible without any token!");
		} catch (UniformInterfaceException exc) {
			assertEquals("The HTTP error code received is not correct", 401, exc.getResponse().getStatus());
		}
		
		// Initialize the TokenRepository
		tested.setClientId("any");
		tested.setClientSecret("any");
		List<String> endPoints = new ArrayList<String>();
		endPoints.add("http://localhost:"+port+"/authorizationServer/authorize");
		tested.setAuthenticationServices(endPoints);
		
		// Enrich a request to an unknown resource
		try {
			tested.enrich(httpClient.path(resource)).get(String.class);
		} catch (UniformInterfaceException exc) {
			assertEquals("The HTTP error code received is not correct", 401, exc.getResponse().getStatus());
		}

		String scope = "";
		TokenResponse token = tested.obtain(resource, scope);
		
		// Adds the return token in the token repository
		tested.add(resource, token);
		// Enrich and trigger a request.
		String result = tested.enrich(httpClient.path(resource)).get(String.class);
		assertEquals("Result returned is unexpected", "Hello World", result);

		// TODO test scopes.
		
	} // testEnrich().

} // Class TestTokenRepository
