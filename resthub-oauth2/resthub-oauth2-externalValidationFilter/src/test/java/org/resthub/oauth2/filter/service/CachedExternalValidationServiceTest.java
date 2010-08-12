package org.resthub.oauth2.filter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.resthub.oauth2.common.model.Token;
import org.resthub.oauth2.filter.mock.authorization.AuthorizationService;

import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * TODO CachedExternalValidationServiceTest documentation
 */
public class CachedExternalValidationServiceTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Static private attributes

	/**
	 * Tested service.
	 */
	protected static ValidationService service;

	/**
	 * Jetty memory server instance.
	 */
	protected static Server authorizationServer;
	
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
		authorizationServer = new Server(port);

		// Configures it
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/authorization");
						
		// Jersey Servlet
		ServletHolder servlet = new ServletHolder(ServletContainer.class);
		servlet.setInitParameter("com.sun.jersey.config.property.packages", 
			"org.resthub.oauth2.filter.mock.authorization");
		context.addServlet(servlet, "/*");
		
		// Jetty start.
		authorizationServer.setHandler(context);
		authorizationServer.start();
		
		// Tested service initialization.
		CachedExternalValidationService myService = new CachedExternalValidationService();
		myService.setAccessTokenParam("access_token");
		myService.setTokenInformationEndpoint("http://localhost:9797/authorization");
		myService.setAuthorizationPassword("p@ssw0rd");
		myService.setDiscardPeriod(0);
		myService.postInit();
		
		service = myService;

	} // suiteSetUp().

	/**
	 * After the test suite, stops the Jetty inmemory server.
	 */
	@AfterClass
	public static void suiteTearDown() throws Exception {
		if (authorizationServer != null) {
			authorizationServer.stop();
		}
	} // suiteTearDown().
	
	/**
	 * Test the retrieval of a token from a mock object.
	 */
	@Test
	public void testValidateToken() {
		// Test a token.
		String accessToken1 = "toto";
		
		// First retrieve
		Token token = service.validateToken(accessToken1);
		assertNotNull("No token retrieved", token);
		assertNotNull("No access token generated", token.accessToken);
		assertNotNull("No refresh token generated", token.refreshToken);

		// Second retrieve
		Token retrieved = service.validateToken(accessToken1);
		assertNotNull("No token retrieved", retrieved);
		assertEquals("Token differs", token, retrieved);
		
		// Test on expired token.
		token = service.validateToken(AuthorizationService.EXPIRED_TOKEN);
		assertNotNull("No token retrieved", token);
		retrieved = service.validateToken(AuthorizationService.EXPIRED_TOKEN);
		assertNotNull("No token retrieved", retrieved);
		assertEquals("Token differs", token, retrieved);

		// Test on unknown token.
		token = service.validateToken(AuthorizationService.UNKNOWN_TOKEN);
		assertNull("Token must not be returned", token);		
	} // testValidateToken().

} // class CachedExternalValidationServiceTest.
