package org.resthub.oauth2.filter.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.resthub.oauth2.common.model.Token;

/**
 * Mock Implementation of token processing service service.
 */
public class MockTokenProcessingService implements TokenProcessingService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void process(Token token, String targetedURI, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// Redirect user to the targeted URI.
		response.sendRedirect(targetedURI);
	} // process().

} // class MockTokenProcessingService
