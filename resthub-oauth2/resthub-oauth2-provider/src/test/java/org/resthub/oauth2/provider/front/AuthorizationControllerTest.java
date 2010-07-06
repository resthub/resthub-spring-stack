package org.resthub.oauth2.provider.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.resthub.oauth2.provider.exception.ProtocolException.Type;
import org.resthub.oauth2.provider.front.model.ObtainTokenErrorResponse;
import org.resthub.oauth2.provider.front.model.TokenResponse;
import org.resthub.oauth2.provider.model.Token;
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
	public void obtainAndRetrieveToken() {
		logger.info("[obtainAndRetrieveToken] ask for a token");
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
		
		logger.info("[obtainAndRetrieveToken] generated token: {}", response);
		assertNotNull("Generated token is null", response);
		assertNotNull("Generated access token is null", response.accessToken);
		assertNotNull("Token doesn't have expire date", response.expiresIn);
		assertNotNull("Generated refresh token is null", response.refreshToken);
		assertNull("Token must not have scope", response.scope);
		
		// Retrieves informations
		Token token = server.path("tokenDetails").queryParam("access_token", response.accessToken).get(Token.class);
		
		logger.info("[obtainAndRetrieveToken] retrieved token: {}", token);
		assertNotNull("Retreived token is null", token);
		assertNotNull("Retreived access token is null", token.accessToken);
		assertNotNull("Token doesn't have expire date", token.lifeTime);
		assertNotNull("Generated refresh token is null", token.refreshToken);
		assertNotNull("Token creation date is null", token.createdOn);
		assertNotNull("Token's permissions are null", token.permissions);
		assertNotNull("Token's user identifier is null", token.userId);
		
		// Compares to original response.
		assertEquals("Retrieved token has not good access token", response.accessToken, token.accessToken);
		assertEquals("Retrieved token has not good refresh token", response.refreshToken, token.refreshToken);
		assertEquals("Retrieved token has not good lifetime", response.expiresIn, token.lifeTime);
	} // obtainAndRetrieveToken().

	/**
	 * Test error cases for token retrieval.
	 */
	@Test
	public void obtainTokenDetailsErrorCase() {
		WebResource server = resource();
		
		try {
			logger.info("[obtainTokenDetailsErrorCase] no access_type");
			server.path("tokenDetails").get(Token.class);
			fail("An UniformInterfaceException must be raised for missing grant_type");
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
