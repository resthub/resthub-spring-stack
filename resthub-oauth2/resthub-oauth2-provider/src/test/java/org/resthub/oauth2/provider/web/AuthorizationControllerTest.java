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

import org.junit.Test;
import org.resthub.oauth2.common.exception.ProtocolException.Error;
import org.resthub.oauth2.common.front.model.ObtainTokenErrorResponse;
import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.oauth2.common.model.Token;
import org.resthub.oauth2.provider.service.MockAuthenticationService;
import org.resthub.web.test.AbstractWebResthubTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;
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
	
} // class AuthorizationControllerTest
