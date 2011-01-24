package org.resthub.oauth2.provider.service;

import java.util.List;

import org.resthub.core.service.GenericService;
import org.resthub.oauth2.common.model.Token;

/**
 * Service interface to manage tokens.
 */
public interface AuthorizationService extends GenericService<Token, Long>{

	/**
	 * Generates a new access token for a given user and for given scopes.<br/><br/>
	 * 
	 * This method is used to generate access tokens with refresh token and possibly access code.<br/><br/>
	 * 
	 * If no user account is found for these credentials, an error is raised.
	 * 
	 * @param scopes List of objet names the client will access with the token. Could be
	 * empty, but not null.<b>TODO Not used no. Must be empty</b>
	 * @param userName The user account's user name.
	 * @param password The user account's password.
	 * @param redirectUri The redirection Uri used to generate an access code.
	 * @return The generated token.
	 * 
	 * @throws ProtocolException INVALID_GRANT: If no user is found for this credentials.
	 * @throws ProtocolException INVALID_SCOPE: If scopes are specified. Currently, no scopes are supported.
	 * @throws IllegalArgumentException when scopes or userName parameter is null.
	 */
	Token generateToken(List<String> scopes, String userName, String password, String redirectUri);

	/**
	 * Adds an access code (with limited lifetime), related to a redirection URI, to a token.<br/>
	 * Save the token in database.
	 * 
	 * @param token Token to wich a code is added.
	 * @param redirectUri The redirection Uri used to generate an access code.
	 * @return The generated token.
	 */
	Token generateCode(Token token, String redirectUri);

	/**
	 * Retrieves all infromation (rights, lifetime, etc...) related to a code.
	 * 
	 * @param code The code related to the searched token.
	 * @param redirectUri The redirection URI related to the code.
	 * 
	 * @return The corresponding token.
	 * 
	 * @throws ProtocolException INVALID_GRANT: If the access code is uknown or expired, or if the redirectionURI 
	 * mismatch.
	 */
	Token getTokenFromCode(String code, String redirectUri);

	/**
	 * Retrieves all infromation (rights, lifetime, etc...) related to an access token
	 * 
	 * @param accessToken The access token found.
	 * @return The corresponding token, or null if the access token is not found.
	 */
	Token getTokenInformation(String accessToken);
	
} // interface AuthorizationService().
