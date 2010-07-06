package org.resthub.oauth2.provider.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.resthub.oauth2.provider.exception.ProtocolException.Type;
import org.resthub.oauth2.provider.front.model.ObtainTokenErrorResponse;
import org.resthub.oauth2.provider.front.model.TokenResponse;
import org.resthub.web.test.AbstractWebResthubTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

/**
 * Test of the front layer, from an Http client point of view.
 */
public class AuthorizationControllerTest extends AbstractWebResthubTest {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes
	
	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	// -----------------------------------------------------------------------------------------------------------------
	// Tests

	/**
	 * Test the protocol implementation of the token obtention. 
	 */
	@Test
	public void obtainToken() {
		logger.info("[obtainToken] ask for a token");
		WebResource server = resource();
		
		Form form = new Form();
		form.add("grant_type", "basic-credentials");
		form.add("client_id", null);
		form.add("client_secret", null);
		form.add("username", "test");
		form.add("password", "t3st");

		// gets a token
		TokenResponse response = server.path("token").
			type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
		
		logger.info("[obtainToken] response: {}", response);
	} // obtainToken().

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
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing grant_type");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.INVALID_REQUEST.value(), response.error);
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
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for unsupported grant_type");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.UNSUPPORTED_GRANT_TYPE.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] missing client_id");
			form = new Form();
			form.add("grant_type", "basic-credentials");
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing client_id");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.INVALID_REQUEST.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] invalid client_id");
			form = new Form();
			form.add("grant_type", "basic-credentials");
			form.add("client_id", "unknown");
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid client_id");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.INVALID_CLIENT_CREDENTIALS.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] missing client_secret");
			form = new Form();
			form.add("grant_type", "basic-credentials");
			form.add("client_id", null);
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing client_secret");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.INVALID_REQUEST.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] invalid client_secret");
			form = new Form();
			form.add("grant_type", "basic-credentials");
			form.add("client_id", null);
			form.add("client_secret", "unknown");
			form.add("username", "test");
			form.add("password", "t3st");
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid client_secret");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.INVALID_CLIENT_CREDENTIALS.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] missing username");
			form = new Form();
			form.add("grant_type", "basic-credentials");
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("password", "t3st");
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing username");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.INVALID_REQUEST.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] missing password");
			form = new Form();
			form.add("grant_type", "basic-credentials");
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("username", "test");
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for missing password");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.INVALID_REQUEST.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] invalid scope");
			form = new Form();
			form.add("grant_type", "basic-credentials");
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			form.add("scope", "123 5435, aDIF");
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for invalid scope");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.INVALID_SCOPE.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

		try {
			logger.info("[obtainToken] unknown scope");
			form = new Form();
			form.add("grant_type", "basic-credentials");
			form.add("client_id", null);
			form.add("client_secret", null);
			form.add("username", "test");
			form.add("password", "t3st");
			form.add("scope", "123 5435 aDIF");
			server.path("token").type(MediaType.APPLICATION_FORM_URLENCODED).post(TokenResponse.class, form);
			fail("An UniformInterfaceException must be raised for unknown scope");
		} catch (UniformInterfaceException exc) {
			assertEquals("HTTP response code is incorrect", 400, exc.getResponse().getStatus());
			// Test response content.
			ObtainTokenErrorResponse response = exc.getResponse().getEntity(ObtainTokenErrorResponse.class);
			assertEquals("Response code is incorrect", Type.INVALID_SCOPE.value(), response.error);
			logger.info("[obtainToken] response returned: {}", response);
		}

	} // obtainToken().
	
} // class AuthorizationControllerTest
