package org.resthub.oauth2.provider.service;

import java.util.ArrayList;
import java.util.List;

import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.oauth2.provider.dao.TokenDao;
import org.resthub.oauth2.provider.exception.ProtocolException;
import org.resthub.oauth2.provider.exception.ProtocolException.Error;
import org.resthub.oauth2.provider.model.Token;
import org.resthub.oauth2.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Authorization service implementation.
 * 
 * Relies on an list of <code>AuthenticationProvider</code> services.
 * This Spring beans is declared within XML configuration files.
 */
public class AuthorizationServiceImpl extends GenericResourceServiceImpl<Token, TokenDao> implements
		AuthorizationService {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes

	/**
	 * Life time of tokens.
	 */
	protected Integer tokenLifeTime = 900;
	
	/**
	 * Length (characters) of tokens.
	 */
	protected Integer tokenLength = 10;
	
	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * List of authentication providers.
	 */
	protected List<AuthenticationService> authenticationProviders;
	
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public methods
	
	/**
	 * Used by Spring to inject a list of authentication providers.
	 * 
	 * @param authenticationProviders List of authentication providers.
	 */
	public void setAuthenticationProviders(
			List<AuthenticationService> authenticationProviders) {
		this.authenticationProviders = authenticationProviders;
	} // setAuthenticationProviders().
	
	/**
	 * Used by Spring to inject the token lifetime (in seconds).
	 * 
	 * @param tokenLifeTime Token lifetime.
	 */
	public void setTokenLifeTime(Integer tokenLifeTime) {
		this.tokenLifeTime = tokenLifeTime;
	} // settokenLifeTime().

	/**
	 * Used by Spring to inject the token tokenLength (number of character).
	 * 
	 * @param tokenLifeTime Token length.
	 */
	public void setTokenLength(Integer tokenLength) {
		this.tokenLength = tokenLength;
	} // setTokenLength().

	// -----------------------------------------------------------------------------------------------------------------
	// Public AuthorizationService inherited methods

	/**
	 * {@inheritDoc}
	 */
	@Transactional(readOnly = false)
	@Override
	public Token generateToken(List<String> scopes, String clientId,
			String clientSecred, String userName, String password) {
		if(logger.isTraceEnabled()) {
			logger.trace("[generateToken] generate new token for scope '{}', userName '{}'", new Object[]{
					StringUtils.collectionToCommaDelimitedString(scopes), userName});
		}
		// Check non-nullity.
		if(scopes == null || userName == null) {
			throw new IllegalArgumentException("scopes nor userName parameter mustn't be null");
		}
		if (scopes.size() != 0) {
			throw new ProtocolException(Error.INVALID_SCOPE, "no scopes are supported");
		}
		// Generate a token.
		Token token = new Token();
		// First, search for credentials in authentication providers.
		if (null != authenticationProviders) {
			for (AuthenticationService provider : authenticationProviders) {
				token.userId = provider.getUser(userName, password);
				// Stores permissions, and leave on the first matching result
				if(token.userId != null) {
					logger.info("[generateToken] Found an account for userName '{}' : {}", userName, token.userId);
					token.permissions = provider.getUserPermissions(token.userId);
					if (token.permissions == null) {
						logger.warn("[generateToken] No permissions for account {} (userName '{}')", token.userId, 
								userName);
						token.permissions = new ArrayList<String>();
					}
					break;
				}
			}
		}
		// No user found
		if(token.userId == null) {
			throw new ProtocolException(Error.INVALID_CLIENT, "no user found for " + userName + 
					" or wrong password");
		}
		// All's fine : generate tokens.
		token.accessToken = Utils.generateString(tokenLength);
		token.refreshToken = Utils.generateString(tokenLength);
		token.lifeTime = tokenLifeTime;
		logger.info("[generateToken] Save token {} for {} during {}", new Object[]{token.accessToken, token.userId, 
				token.lifeTime});
		// Save the token.
		token = dao.save(token);
		return token;
	} // generateToken().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token getTokenInformation(String accessToken) {
		// Check non-nullity.
		if(accessToken == null) {
			throw new IllegalArgumentException("accessToken parameter mustn't be null");
		}
		logger.trace("[getTokenInformation] retrieves information with access token {}", accessToken);
		// Retrieves in database: returns an array, but as accessToken is unic, only one object will be returned.
		List<Token> tokens = dao.findEquals("accessToken", accessToken);
		Token result = null;
		if(tokens.size() > 0) {
			result = tokens.get(0);
		}
		return result;
	} // getTokenInformation().
	
} // class AuthorizationServiceImpl.
