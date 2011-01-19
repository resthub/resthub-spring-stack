package org.resthub.oauth2.test.authorizationService;

import java.util.ArrayList;
import java.util.List;

import org.resthub.oauth2.provider.service.AuthenticationService;

/**
 * This mock authentication service is used to answer predefined response to 
 * token obtention requests.
 */
public class MockAuthenticationService implements AuthenticationService {

	/**
	 * Used for test case that need authentication reject.
	 */
	public static final String UNKNOWN_CLIENT_ID = "unknown";
	
	/**
	 * Used for test case that need authentication reject.
	 */
	public static final String UNKNOWN_CLIENT_SECRET = "aaaaa";

	/**
	 * Used for test case that need a user_id with ADMIN right
	 */
	public static final String ADMIN_USER_ID = "E515sZ";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUser(String userName, String password) {
		String user = null;
		if(UNKNOWN_CLIENT_ID.compareTo(userName) != 0 && UNKNOWN_CLIENT_SECRET.compareTo(password) != 0) {
			user = ADMIN_USER_ID;
		}
		return  user;
	} // getUser().

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getUserPermissions(String userId) {
		List<String> permissions = new ArrayList<String>();
		if(ADMIN_USER_ID.compareTo(userId) == 0) {
			permissions.add("ADMIN");
		}
		return permissions;
	} // getUserPermissions().

} // class MockAuthenticationService
