package org.resthub.oauth2.filter.service;

import org.resthub.oauth2.common.model.Token;

/**
 * Service used to validate tokens.<br/>
 * Validation on the presence and syntax of the access token have been made, so semantic checks must be implemented 
 * here.<br/><br/>
 * 
 * The scope validation is done with the permissions management (JSR 250 annotations) and the error response done with 
 * WebApplicationExceptionHandler.
 */
public interface ValidationService {

	/**
	 * Validates the token and returns all token informations.<br/>
	 * If no token information is found, returns null. otherwise, return the permissions.
	 * <br/><br/>
	 * 
	 * Validation on the presence and syntax of the access token have been made, so semantic checks must be implemented 
	 * here.
	 * 
	 * @param accessToken The access token extracted from the request.Never null.
	 * @return The corresponding token informations. Null if no token information where found.
	 */
	Token validateToken(String accessToken);
	
} // ValidationService
