package org.resthub.oauth2.provider.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.service.AbstractServiceTest;
import org.resthub.oauth2.common.exception.ProtocolException;
import org.resthub.oauth2.common.exception.ProtocolException.Error;
import org.resthub.oauth2.common.model.Token;

/**
 * Test class for authorization service.
 */
public class AuthorizationServiceImplTest extends AbstractServiceTest<Token, Long, AuthorizationService>{

	// -----------------------------------------------------------------------------------------------------------------
	// Attributes

	/**
	 * Sets the tested service implementation
	 * 
	 * @param service: the tested service.
	 */
	@Inject
	@Named("authorizationService")
	@Override
	public void setService(AuthorizationService Service) {
		super.setService(Service);
	} // setservice

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getIdFromObject(Token obj) {
		Long id = null;
		if (obj != null) {
			id = obj.id;
		}
		return id;
	} // getIdFromObject().

	// -----------------------------------------------------------------------------------------------------------------
	// Tests

	/**
	 * Tests the creation, update and removal of a token.
	 */
	@Override
	public void testUpdate() throws Exception {
		Token token = new Token();
		token.accessToken = "XXXXXX";
		token.userId = "789456";
		token.permissions.add(MockAuthenticationService.ADMIN_RIGHT);
		token.permissions.add(MockAuthenticationService.USER_RIGHT);
		
		// saves a news user in DB
		token = service.create(token);

		assertNotNull("token has not been created", token);

		assertNotNull("database id has not been generated", token.id);

		// retrieves a user by his id
		Token retrieved = service.findById(token.id);

		assertEquals("token created has changed : finder not valid", token, retrieved);
		assertEquals("token's value was not persisted", token.accessToken, retrieved.accessToken);
		assertEquals("token's creation date was not persisted", token.createdOn, retrieved.createdOn);
		assertEquals("token's permissions was not persisted", token.permissions, retrieved.permissions);
		assertTrue("token's admin right was lost", retrieved.permissions.contains(MockAuthenticationService.ADMIN_RIGHT));
		assertTrue("token's user right was lost", retrieved.permissions.contains(MockAuthenticationService.USER_RIGHT));
		assertEquals("token's user id was not persisted", token.userId, retrieved.userId);

		String newValue = "YYYYYY";
		String newUserId = "654321";
		retrieved.accessToken = newValue;
		retrieved.createdOn.setTime(retrieved.createdOn.getTime()+5000);
		retrieved.permissions.remove(MockAuthenticationService.ADMIN_RIGHT);
		retrieved.userId = newUserId;

		// updates the user and checks new values
		Token updated = service.update(retrieved);

		assertEquals("token's id has changed", token.id, updated.id);
		assertEquals("token's value should have changed", newValue, updated.accessToken);
		assertEquals("token's user id name should have changed", newUserId, updated.userId);
		assertFalse("token's admin right still here", retrieved.permissions.contains(
				MockAuthenticationService.ADMIN_RIGHT));
		assertTrue("token's user right was lost", retrieved.permissions.contains(MockAuthenticationService.USER_RIGHT));

		// deletes the user
		service.delete(updated);

		assertNull("token not deleted", service.findById(updated.id));
	} // testUpdate().
	
	/**
	 * Test the token generation failure cases.
	 */
	@Test
	public void generateAccessTokenErrors() {
		try {
			service.generateToken(null, "", "", "", "");
			fail("An IllegalArgumentException must be raised for null scopes parameter");
		} catch (IllegalArgumentException exc) {
			// All things right
		}		
		try {
			service.generateToken(new ArrayList<String>(), null, null, null, "");
			fail("An IllegalArgumentException must be raised for null userName parameter");
		} catch (IllegalArgumentException exc) {
			// All things right
		}		
		try {
			service.generateToken(new ArrayList<String>(), null, null, 
					MockAuthenticationService.UNKNOWN_USERNAME, null);
			fail("A ProtocolException must be raised for unknown userName");
		} catch (ProtocolException exc) {
			// All things right
			assertEquals("The error case is not good", Error.INVALID_CLIENT, exc.errorCase);
		}		
		try {
			List<String> scopes = new ArrayList<String>();
			scopes.add("unknown");
			service.generateToken(scopes, null, null, "someone", null);
			fail("A ProtocolException must be raised for unknown scope");
		} catch (ProtocolException exc) {
			// All things right
			assertEquals("The error case is not good", Error.INVALID_SCOPE, exc.errorCase);
		}		
	} // generateAccessTokenErrors().

	/**
	 * Test the token generation.
	 */
	@Test
	public void generateAccessToken() {
		String userName = "test";
		String password = "t3st";
		
		// Generates token.
		Token token = service.generateToken(new ArrayList<String>(), null, null, userName, password);
		assertNotNull("No token generated", token);
		assertNotNull("Token does not have database id", token.id);
		assertNotNull("No access token generated", token.accessToken);
		assertNotNull("No refresh token generated", token.refreshToken);
		assertFalse("Refresh and access token must be different", token.refreshToken.compareTo(token.accessToken) == 0);
		
		// Gets the token from database to check its existence.
		assertEquals("Token not serialized", token, service.findById(token.id));		
	} // generateAccessToken().

	
	/**
	 * Test the token retrieval failure cases.
	 */
	@Test
	public void getTokenInformationErrors() {
		try {
			service.getTokenInformation(null);
			fail("An IllegalArgumentException must be raised for null scopes parameter");
		} catch (IllegalArgumentException exc) {
			// All things right
		}		
	} // getTokenInformationErrors().

	/**
	 * Test the token retrieval.
	 */
	@Test
	public void getTokenInformation() {
		assertNull("No token may have been retrieved !", service.getTokenInformation("unknown"));

		String userName = "test";
		String password = "t3st";

		// Generates token.
		Token token = service.generateToken(new ArrayList<String>(), null, null, userName, password);
		assertNotNull("No token generated", token);
		assertNotNull("No access token generated", token.accessToken);
		
		// Retreives token.
		Token retrievedToken = service.getTokenInformation(token.accessToken);
		assertNotNull("No token retrieved", retrievedToken);
		assertEquals("Tokens not equals", token, retrievedToken);
		assertEquals("Access tokens not equals", token.accessToken, retrievedToken.accessToken);
		assertEquals("User ids not equal", token.userId, retrievedToken.userId);
		assertEquals("Refresh tokens not equal", token.refreshToken, retrievedToken.refreshToken);
		assertEquals("Creation dates not equal", token.createdOn, retrievedToken.createdOn);
		assertEquals("Lifetimes not equal", token.lifeTime, retrievedToken.lifeTime);		
		assertTrue("token's admin right was lost", retrievedToken.permissions.contains(
				MockAuthenticationService.ADMIN_RIGHT));
		assertTrue("token's user right was lost", retrievedToken.permissions.contains(
				MockAuthenticationService.USER_RIGHT));

		// Generates another token for user with no permissions.
		token = service.generateToken(new ArrayList<String>(), null, null, 
				MockAuthenticationService.NO_PERMISSIONS_USERNAME, password);
		assertNotNull("No token generated", token);
		assertNotNull("No access token generated", token.accessToken);
		
		// Retreives token.
		retrievedToken = service.getTokenInformation(token.accessToken);
		assertNotNull("No token retrieved", retrievedToken);
		assertEquals("Tokens not equals", token, retrievedToken);
		assertEquals("Access tokens not equals", token.accessToken, retrievedToken.accessToken);
		assertEquals("User ids not equal", token.userId, retrievedToken.userId);
		assertEquals("Refresh tokens not equal", token.refreshToken, retrievedToken.refreshToken);
		assertEquals("Creation dates not equal", token.createdOn, retrievedToken.createdOn);
		assertEquals("Lifetimes not equal", token.lifeTime, retrievedToken.lifeTime);		
		assertEquals("token has got permissions", 0, retrievedToken.permissions.size());

	} // getTokenInformation().

} // Classe AuthorizationServiceImplTest
