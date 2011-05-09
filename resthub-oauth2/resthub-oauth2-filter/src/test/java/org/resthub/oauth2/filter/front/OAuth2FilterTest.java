package org.resthub.oauth2.filter.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.http.HttpHeaders;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.resthub.oauth2.filter.service.MockExternalValidationService;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

/**
 * Check the OAuth2Filter functionnalities.
 */
public class OAuth2FilterTest extends AbstractOAuth2FilterTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Test suite initialization and finalization

	/**
	 * Before the test suite, launches a Jetty inmemory server.
	 */
	@BeforeClass
	public static void suiteSetUp() throws Exception {
		// Starts the resource server
		startResourceServer();
		// Starts the authorization server
		startAuthorizationServer();
	} // suiteSetUp().
	
	// -----------------------------------------------------------------------------------------------------------------
	// Tests

	/**
	 * Tests access to different hello world urls that are restricted.
	 */
	@Test
	@Ignore
	// TODO : reactivate it when fixed
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
	@Ignore
	// TODO : reactivate it when fixed
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
					MockExternalValidationService.UNKNOWN_TOKEN).get(String.class);
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
					MockExternalValidationService.EXPIRED_TOKEN).get(String.class);
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
