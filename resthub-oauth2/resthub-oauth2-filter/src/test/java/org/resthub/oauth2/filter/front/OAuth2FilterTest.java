package org.resthub.oauth2.filter.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.oauth2.filter.mock.authorization.AuthorizationService;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

/**
 * TODO OAuth2FilterTest documentation
 */
public class OAuth2FilterTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Static private attributes

	/**
	 * Jetty memory server instance for resource server.
	 */
	protected static Server resourceServer;
	
	/**
	 * Jetty memory server instance for authorization server.
	 */
	protected static Server authorizationServer;
	
	/**
	 * Jetty memory server port.
	 */
	protected static int resourceServerPort = 9796;
	
	/**
	 * Jetty memory server port.
	 */
	protected static int authorizationServerPort = 9797;
 
	/**
	 * Spring configuration locations
	 */
	protected static String contextLocations = "classpath*:resthubContext.xml classpath:applicationContext.xml";

	// -----------------------------------------------------------------------------------------------------------------
	// Test suite initialization and finalization

	/**
	 * Before the test suite, launches a Jetty inmemory server.
	 */
	@BeforeClass
	public static void suiteSetUp() throws Exception {
		// Creates a Jetty for the resource server.
		resourceServer = new Server(resourceServerPort);

		// Configures it
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/inmemory");
		
		// Spring configuration.
		context.getInitParams().put("contextConfigLocation", contextLocations);
        context.addEventListener(new ContextLoaderListener());

		// Tested filter.
        FilterHolder filter = new FilterHolder(DelegatingFilterProxy.class);
        // Name must be the same as the filter bean name.
        filter.setName("oauth2Filter");
		context.addFilter(filter, "/*", FilterMapping.REQUEST);
		
		// Jersey Servlet
		ServletHolder servlet = new ServletHolder(SpringServlet.class);
		servlet.setInitParameter("com.sun.jersey.spi.container.ResourceFilters", 
				"com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory");
		context.addServlet(servlet, "/*");
		
		// Jetty start.
		resourceServer.setHandler(context);
		resourceServer.start();
		
		
		// Creates a Jetty for the authorization server.
		authorizationServer = new Server(authorizationServerPort);

		// Configures it
		context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/authorization");
						
		// Jersey Servlet
		servlet = new ServletHolder(ServletContainer.class);
		servlet.setInitParameter("com.sun.jersey.config.property.packages", 
				"org.resthub.oauth2.filter.mock.authorization");
		context.addServlet(servlet, "/*");
		
		// Jetty start.
		authorizationServer.setHandler(context);
		authorizationServer.start();
	} // suiteSetUp().

	/**
	 * After the test suite, stops the Jetty inmemory server.
	 */
	@AfterClass
	public static void suiteTearDown() throws Exception {
		if (resourceServer != null) {
			resourceServer.stop();
		}
		if (authorizationServer != null) {
			authorizationServer.stop();
		}
	} // suiteTearDown().

	// -----------------------------------------------------------------------------------------------------------------
	// Protected methods

	/**
	 * Returns an object to interact with the inmemory server.
	 * 
	 * @return A Jersey HTTP client.
	 */
	protected WebResource resource() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		return client.resource("http://localhost:" + resourceServerPort +"/inmemory");
	} // resource()

	/**
	 * Checks the syntax of authenticate header, and extract the error case.
	 * 
	 * @param header The 
	 * @return The error case. Can be null.
	 */
	protected String checkAuthenticateHeader(ClientResponse response) {
		assertTrue("Authenticate header is not present", response.getHeaders().containsKey(
				HttpHeaders.WWW_AUTHENTICATE));
		String error = response.getHeaders().get(HttpHeaders.WWW_AUTHENTICATE).get(0);
		assertTrue("Authenticate header value is not valid", error.matches("Token realm=\".*\"(, error=\".*\"(, " +
				"error_description=\".*\"(, error_uri=\".*\")?)?)?"));
		Pattern p = Pattern.compile("error=\"([^\"]*)\"");
		Matcher m = p.matcher(error);
		m.find();
		String cause = null;
		try {
			cause = m.group(1);
		} catch (IllegalStateException exc) {
			// No match found.
		}
		return cause;
	} // checkAuthenticateHeader().
	
	// -----------------------------------------------------------------------------------------------------------------
	// Tests

	/**
	 * Tests access to different hello world urls that are restricted.
	 */
	@Test
	public void accessRestriction() {
		WebResource server = resource();
		String result = "";
		// Access free url  with header
		result = server.path("/").header(HttpHeaders.AUTHORIZATION, "OAuth toto").get(String.class);
		assertEquals("The result is incorrect", "Hello world", result);
 
		// Access free url with parameter
		result = server.path("/").queryParam("oauth_token", "toto").get(String.class);
		assertEquals("The result is incorrect", "Hello world", result);

		// Access free url with form
		Form form = new Form();
		form.add("oauth_token", "toto");
		result = server.path("/post").type(MediaType.APPLICATION_FORM_URLENCODED).post(String.class, form);
		assertEquals("The result is incorrect", "Hello world", result);

		// Access protected admin url with header
		result = server.path("/admin").header(HttpHeaders.AUTHORIZATION, "OAuth toto").get(String.class);
		assertEquals("The result is incorrect", "Hello world Admin", result);

		// Access protected admin url with parameter
		result = server.path("/admin").queryParam("oauth_token", "OAuth toto").get(String.class);
		assertEquals("The result is incorrect", "Hello world Admin", result);

		// Access protected admin url with form
		form = new Form();
		form.add("oauth_token", "OAuth toto");
		result = server.path("/postadmin").type(MediaType.APPLICATION_FORM_URLENCODED).post(String.class, form);
		assertEquals("The result is incorrect", "Hello world Admin", result);

		// Access protected other url
		try {
			result = server.path("/other").header(HttpHeaders.AUTHORIZATION, "OAuth toto").get(String.class);
			fail("An UniformInterfaceException must be raised for protected other access");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 403, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "insufficient_scope", cause);
		}

		// Access unavailable url
		try {
			result = server.path("/secured").header(HttpHeaders.AUTHORIZATION, "OAuth toto").get(String.class);
			fail("An UniformInterfaceException must be raised for unavailable access");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 403, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "insufficient_scope", cause);
		}

	} // accessRestriction().
		
	/**
	 * Tests the filter error cases.
	 */
	@Test
	public void errorCase() {
		WebResource server = resource();
		
		// Simple hit without anything.
		try {
			server.path("/").get(String.class);
			fail("An UniformInterfaceException must be raised for request without token");
		} catch( UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 401, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertNull("error is not good", cause);
		}
		
		// Simple hit with invalid header.
		try {
			server.path("/").header(HttpHeaders.AUTHORIZATION, "Toto").get(String.class);
			fail("An UniformInterfaceException must be raised for request with invalid header");
		} catch( UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "invalid_request", cause);
		}
		
		// Simple hit with unknown accessToken.
		try {
			server.path("/").header(HttpHeaders.AUTHORIZATION, "OAuth "+
					AuthorizationService.UNKNOWN_TOKEN).get(String.class);
			fail("An UniformInterfaceException must be raised for request with unknown token");
		} catch( UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 401, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "invalid_token", cause);
		}
		
		// Simple hit with expired accessToken.
		try {
			server.path("/").header(HttpHeaders.AUTHORIZATION, "OAuth "+
					AuthorizationService.EXPIRED_TOKEN).get(String.class);
			fail("An UniformInterfaceException must be raised for request with expired token");
		} catch( UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 401, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "expired_token", cause);
		}

		// Simple hit with too many accessToken.
		try {
			server.path("/").queryParam("oauth_token", "toto").
					header(HttpHeaders.AUTHORIZATION, "OAuth toto").get(String.class);
			fail("An UniformInterfaceException must be raised for request with expired token");
		} catch( UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "invalid_request", cause);
		}

	} // errorCase().
	
} // class OAuth2FilterTest
