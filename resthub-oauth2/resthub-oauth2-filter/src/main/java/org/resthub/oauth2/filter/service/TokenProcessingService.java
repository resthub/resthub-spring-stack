package org.resthub.oauth2.filter.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.resthub.oauth2.common.front.model.TokenResponse;

/**
 * Service used to process tokens obtained with the Web-server profile.<br/>
 * Token is obtained after redirecting the end-user to the Authorization Server.<br/><br/>
 * 
 * Then, this service is invoked with the obtained token to perform specific task, like cookie generation.<br/><br/>
 */
public interface TokenProcessingService {

	/**
	 * Process the OAuth2 token retrived from the Authorization Server.<br/>
	 * 
	 * @param token The OAuth2 token corresponding to the connected end-user.
	 * @param targetURI The initial URI targeted by the end-user.
	 * @param request Incomming Http request.
	 * @param response outgoing Http response.
	 */
	void process(TokenResponse token, String targetedURI, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

} // interface TokenProcessingService
