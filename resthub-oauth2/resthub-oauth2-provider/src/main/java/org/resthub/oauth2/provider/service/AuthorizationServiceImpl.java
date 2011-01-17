package org.resthub.oauth2.provider.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.resthub.core.service.GenericServiceImpl;
import org.resthub.oauth2.common.exception.ProtocolException;
import org.resthub.oauth2.common.exception.ProtocolException.Error;
import org.resthub.oauth2.common.model.Token;
import org.resthub.oauth2.provider.dao.TokenDao;
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
public class AuthorizationServiceImpl extends GenericServiceImpl<Token, TokenDao, Long> implements
		AuthorizationService {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes

	/**
	 * Life time of tokens (in seconds).
	 */
	protected Integer tokenLifeTime = 900;

	/**
	 * Life time of code (in seconds).
	 */
	protected Integer codeLifeTime = 180;

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
	public void setTokenLifeTime(Integer codeLifeTime) {
		this.codeLifeTime = codeLifeTime;
	} // setTokenLifeTime().

	/**
	 * Used by Spring to inject the code lifetime (in seconds).
	 * 
	 * @param codeLifeTime Code lifetime.
	 */
	public void setCodeLifeTime(Integer codeLifeTime) {
		this.codeLifeTime = codeLifeTime;
	} // setCodeLifeTime().

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
	public Token generateToken(List<String> scopes, String userName, String password, String redirectUri) {
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
					token.permissions = new ArrayList<String>(provider.getUserPermissions(token.userId));
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
			throw new ProtocolException(Error.INVALID_GRANT, "no user found for " + userName + 
					" or wrong password");
		}
		// All's fine : generate tokens.
		token.accessToken = Utils.generateString(tokenLength);
		token.refreshToken = Utils.generateString(tokenLength);
		token.lifeTime = tokenLifeTime;
		// Possible code generation.
		if (redirectUri != null) {
			token.code = Utils.generateString(tokenLength);
			token.redirectUri = redirectUri;
			token.codeExpiry = new Date().getTime()+codeLifeTime*1000;
		}
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
	
	/**
	 * #{@inheritDoc}
	 */
	@Override
	public Token getTokenFromCode(String code, String redirectUri) {
		// Check non-nullity.
		if(code == null || redirectUri == null) {
			throw new IllegalArgumentException("redirectUri and code parameters mustn't be null");
		}
		logger.trace("[getTokenFromCode] retrieves information with code {}", code);
		// Retrieves in database: returns an array, but as accessCode is unic, only one object will be returned.
		List<Token> tokens = dao.findEquals("code", code);
		Token result = null;
		if(tokens.size() > 0) {
			result = tokens.get(0);
		} else {
			logger.debug("[obtainAccessToken] invalid access code {}", code);
			throw new ProtocolException(Error.INVALID_GRANT, "access code is unknown");			
		}
		// Checks token lifetime and redirection URI
		if (new Date().getTime() > result.codeExpiry) {
			logger.debug("[obtainAccessToken] expired access code {}", code);
			throw new ProtocolException(Error.INVALID_GRANT, "access code is expired");				
		}
		if (!redirectUri.equals(result.redirectUri)) {
			logger.debug("[obtainAccessToken] redirection URI mismatch {}", code);
			throw new ProtocolException(Error.INVALID_GRANT, "redirection URI mismatch");				
		}
		return result;
	} // getTokenFromCode().
	
} // class AuthorizationServiceImpl.
