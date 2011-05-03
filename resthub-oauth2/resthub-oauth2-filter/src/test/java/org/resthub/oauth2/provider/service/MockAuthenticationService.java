package org.resthub.oauth2.provider.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Test mock used to gets users and permissions during tests.
 */
@Named("mockAuthenticationService")
@Singleton
public class MockAuthenticationService {

	// -----------------------------------------------------------------------------------------------------------------
	// Public constants

	/**
	 * Administrator permission.
	 */
	public static final String ADMIN_RIGHT = "ADMIN";
	
	/**
	 * User permission.
	 */
	public static final String USER_RIGHT = "USER";

	/**
	 * UserName used to get null result with <code>getUser().</code>
	 */
	public static final String UNKNOWN_USERNAME = "unknown";

	/**
	 * UserName used to get NO_PERMISSIONS_USER_ID result with <code>getUser().</code>
	 */
	public static final String NO_PERMISSIONS_USERNAME = "toto";

	/**
	 * UserId used to get empty result with <code>getUserPermissions().</code>
	 */
	public static final String NO_PERMISSIONS_USER_ID = "123456";

	// -----------------------------------------------------------------------------------------------------------------
	// Public AuthenticationService inherited methods

	/**
	 * This mock implementation returns always a random user Id, except when passing 
	 * UNKNOWN_USERNAME.
	 * It returns the NO_PERMISSIONS_USER_ID when passing NO_PERMISSIONS_USERNAME
	 * 
	 * @param userName The searched user name.
	 * @param password The corresponding password.
	 * @return Identifier of the user account. Null if no account found.
	 */
	
	public String getUser(String userName, String password) {
		String userId = null;
		if(UNKNOWN_USERNAME.compareTo(userName) != 0) {
			if (NO_PERMISSIONS_USERNAME.compareTo(userName) == 0) {
				userId = NO_PERMISSIONS_USER_ID;
			} else {
				userId = new Random().nextLong()+userName;
			}
		}
		return userId;
	} // getUser().

	/**
	 * This mock implementation returns always an array with "ADMIN" and "USER" except when passing 
	 * NO_PERMISSIONS_USER_ID.
	 * 
	 * @param userId an existing userId
	 * @return List of the user's permissions.
	 */
	
	public List<String> getUserPermissions(String userId) {
		List<String> permissions = new ArrayList<String>();
		if (NO_PERMISSIONS_USER_ID.compareTo(userId) != 0) {
			permissions.add("ADMIN");
			permissions.add("USER");
		}
		return permissions;
	} // getUserPermissions().
	
} // class MockAuthenticationService().
