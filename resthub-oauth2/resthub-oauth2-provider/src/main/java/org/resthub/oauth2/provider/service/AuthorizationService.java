package org.resthub.oauth2.provider.service;

import java.util.List;

import org.resthub.core.service.GenericResourceService;
import org.resthub.oauth2.provider.exception.ProtocolException;
import org.resthub.oauth2.provider.model.Token;

/**
 * Service interface to manage tokens.
 */
public interface AuthorizationService extends GenericResourceService<Token>{

	/**
	 * Generates a new access token for a given user and for given scopes.<br/><br/>
	 * 
	 * This method is used to generate access tokens in the gant type "Resource Owner Basic Credentials".<br/><br/>
	 * 
	 * If no user account is found for these credentials, an error is raised.
	 * 
	 * @param scopes List of objet names the client will access with the token. Could be
	 * empty, but not null.
	 * @param clientId The client unic identifier. <b>Not used now, must be null</b>.
	 * @param clientSecred The client shared secret. <b>Not used now, must be null</b>.
	 * @param userName The user account's user name.
	 * @param password The user account's password.
	 * @return The generated token.
	 * 
	 * @throws ProtocolException INVALID_CLIENT_CREDENTIALS: If no user is found for this credentials.
	 * @throws ProtocolException INVALID_SCOPE: If scopes are specified. Currently, no scopes are supported.
	 * @throws IllegalArgumentException when scopes or userName parameter is null.
	 */
	Token generateToken(List<String> scopes, String clientId, String clientSecred, String userName, 
			String password);
	
	/**
	 * Retrieves all infromation (rights, lifetime, etc...) related to an access token
	 * 
	 * @param accessToken The access token found.
	 * @return The corresponding token, or null if the access token is not found.
	 */
	Token getTokenInformation(String accessToken);
	
} // interface AuthorizationService().
