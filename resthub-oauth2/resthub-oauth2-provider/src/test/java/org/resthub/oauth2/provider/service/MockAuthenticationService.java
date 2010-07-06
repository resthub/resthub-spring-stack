package org.resthub.oauth2.provider.service;

import java.util.List;
import java.util.Random;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Test mock used to gets users and permissions during tests.
 */
@Named("mockAuthenticationService")
@Singleton
public class MockAuthenticationService implements AuthenticationService {

	/**
	 * UserName used to get null result with <code>getUser().</code>
	 */
	public static final String UNKNOWN_USERNAME = "unknown";

	/**
	 * This mock implementation returns always a random user Id, except when passing 
	 * UNKNOWN_USERNAME.
	 * 
	 * @param userName The searched user name.
	 * @param password The corresponding password.
	 * @return Identifier of the user account. Null if no account found.
	 */
	@Override
	public String getUser(String userName, String password) {
		String userId = null;
		if(userName.compareTo(UNKNOWN_USERNAME) != 0) {
			userId = new Random().nextLong()+userName;
		}
		return userId;
	} // getUser().

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getUserPermissions(String userId) {
		// TODO Auto-generated method stub
		return null;
	} // getUserPermissions().
	
} // class MockAuthenticationService().
