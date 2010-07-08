package org.resthub.oauth2.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.oauth2.filter.dao.MockTokenDao;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

public class OAuth2FilterTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Static private attributes

	/**
	 * Jetty memory server instance.
	 */
	protected static Server server;
	
	/**
	 * Jetty memory server port.
	 */
	protected static int port = 9797;
 
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
		// Creates a Jetty server.
		server = new Server(port);

		// Configures it
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/inmemory");
		
		// Spring configuration.
		context.getInitParams().put("contextConfigLocation", contextLocations);
        context.addEventListener(new ContextLoaderListener());

        // Jersy-Spring servlet
		context.addServlet(SpringServlet.class, "/*");

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
		server.setHandler(context);
		server.start();
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
	// Protected methods

	/**
	 * Returns an object to interact with the inmemory server.
	 * 
	 * @return A Jersey HTTP client.
	 */
	protected WebResource resource() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		return client.resource("http://localhost:" + port +"/inmemory");
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
				"error-description=\".*\"(, error-uri=\".*\")?)?)?"));
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
		// Access free url.
		result = server.path("/").header(HttpHeaders.AUTHORIZATION, "Token token=\"toto\"").get(String.class);
		assertEquals("The result is incorrect", "Hello world", result);
		
		// Access protected admin url
		result = server.path("/admin").header(HttpHeaders.AUTHORIZATION, "Token token=\"toto\"").get(String.class);
		assertEquals("The result is incorrect", "Hello world Admin", result);

		// Access protected other url
		try {
			result = server.path("/other").header(HttpHeaders.AUTHORIZATION, "Token token=\"toto\"").get(String.class);
			fail("An UniformInterfaceException must be raised for protected other access");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 403, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "insufficient-scope", cause);
		}

		// Access unavailable url
		try {
			result = server.path("/secured").header(HttpHeaders.AUTHORIZATION, "Token token=\"toto\"").get(String.class);
			fail("An UniformInterfaceException must be raised for unavailable access");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 403, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "insufficient-scope", cause);
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
			assertEquals("error is not good", "invalid-request", cause);
		}
		
		// Simple hit with unknown accessToken.
		try {
			server.path("/").header(HttpHeaders.AUTHORIZATION, "Token token=\""+MockTokenDao.UNKNOWN_TOKEN+"\"").get(
					String.class);
			fail("An UniformInterfaceException must be raised for request with unknown token");
		} catch( UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 401, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "invalid-token", cause);
		}
		
		// Simple hit with expired accessToken.
		try {
			server.path("/").header(HttpHeaders.AUTHORIZATION, "Token token=\""+MockTokenDao.EXPIRED_TOKEN+"\"").get(
					String.class);
			fail("An UniformInterfaceException must be raised for request with expired token");
		} catch( UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 401, exc.getResponse().getStatus());
			// Test response content.
			String cause = checkAuthenticateHeader(exc.getResponse());
			assertEquals("error is not good", "expired-token", cause);
		}

	} // errorCase().
	
} // class OAuth2FilterTest
