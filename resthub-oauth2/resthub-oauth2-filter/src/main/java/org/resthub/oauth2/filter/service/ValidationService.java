package org.resthub.oauth2.filter.service;

import org.resthub.oauth2.filter.front.WebApplicationExceptionHandler;
import org.resthub.oauth2.provider.model.Token;


/**
 * Service used to validate tokens.<br/>
 * Validate presence and syntax of the access token, and checks its semantic existence.<br/><br/>
 * 
 * The scope validation is done with the permissions management (JSR 250 annotations) and the error response done with 
 * {@link WebApplicationExceptionHandler}.
 */
public interface ValidationService {

	/**
	 * Validates the token and returns all token informations.
	 * 
	 * @param headerValue The value of the Authorization HTTP header. May be null.
	 * @return The corresponding token informations. Never null.
	 * 
	 * @throws ProtocolException - UNAUTHORIZED_REQUEST when the header information is null.
	 * @throws ProtocolException - INVALID_REQUEST when the header information is invalid or the access token unknown.
	 * @throws ProtocolException - EXPIRED_TOKEN when the access token needs to be refreshed.
	 */
	Token validateToken(String headerValue);
	
} // ValidationService
