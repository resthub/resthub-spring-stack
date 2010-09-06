package org.resthub.oauth2.filter.service;


import java.util.Date;

import org.resthub.oauth2.common.model.Token;
import org.resthub.oauth2.utils.Utils;

/**
 * Mock Implementation of validation service.
 */
public class MockExternalValidationService implements ValidationService {

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
	 * Access token used to get a null response with <code>getByAccessToken().</code>
	 */
	public static final String UNKNOWN_TOKEN = "unknown";

	/**
	 * Access token used to get an expired token with <code>getByAccessToken().</code>
	 */
	public static final String EXPIRED_TOKEN = "expired";

	/**
	 * Access token used to get a token without permissions with <code>getByAccessToken().</code>
	 */
	public static final String NO_PERMISSION_TOKEN = "no-permissions";

	// -----------------------------------------------------------------------------------------------------------------
	// Public TokenDao inherited methods

	/**
	 * Retrieve all token information from an access token.<br/>
	 * Always return a valid token with permissions, except if access token is equals to:
	 * <ul>
	 * <li>UNKNOWN_TOKEN: the response will be null</li>
	 * <li>EXPIRED_TOKEN: the returned token will be outdated</li>
	 * <li>NO_PERMISSION_TOKEN: the returned token will have no permissions</li>
	 * </ul>
	 * 
	 * @param accessToken The access token searched.
	 * @return All token information, or null if no token found.
	 */
	@Override
	public Token validateToken(String accessToken) {
		Token token = null;
		if(UNKNOWN_TOKEN.compareTo(accessToken) != 0) {
			token = new Token();
			token.accessToken = Utils.generateString(15);
			token.refreshToken = Utils.generateString(15);
			token.createdOn = new Date();
			// Token created 5 minutes ago
			token.createdOn.setTime(token.createdOn.getTime()-(5*60*1000));
			token.lifeTime = 900;
			token.userId = Utils.generateString(5);
			token.id = (long)Math.random()*1000;
			// Permissions
			token.permissions.add(ADMIN_RIGHT);
			token.permissions.add(USER_RIGHT);
			
			if (EXPIRED_TOKEN.compareTo(accessToken) == 0) {
				// Token created 20 minutes ago
				token.createdOn.setTime(token.createdOn.getTime()-(15*60*1000));
			} else if (NO_PERMISSION_TOKEN.compareTo(accessToken) == 0) {
				token.permissions.clear();
			}
		}
		return token;
	} // getByAccessToken().
	
} // class MockTokenDao.
