package org.resthub.oauth2.provider.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.resthub.oauth2.common.exception.ProtocolException;
import org.resthub.oauth2.common.exception.ProtocolException.Error;
import org.resthub.oauth2.common.front.model.TokenResponse;
import org.resthub.oauth2.common.model.Token;
import org.resthub.oauth2.provider.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
/**
 * Authorization controller implementation.
 */
@Named("authorizationController")
@Singleton
public class AuthorizationControllerImpl implements AuthorizationController {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes
	
	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Inject the service layer.
	 */	
	@Inject
	@Named("authorizationService")
	protected AuthorizationService service;
	
	/**
	 * Inject the auhorization password to obtain token information.
	 */
	@Value("#{securityConfig.authorizationPassword}")
	protected String authorizationPassword = "pAss?w0rd";
	
	/**
	 * Inject the cookie name.
	 */
	@Value("#{securityConfig.cookieName}")
	protected String cookieName = "oauth_token";

	/**
	 * Inject the cookie domain.
	 */
	@Value("#{securityConfig.cookieDomain}")
	protected String cookieDomain = "";

	/**
	 * Inject the cookie path.
	 */
	@Value("#{securityConfig.cookiePath}")
	protected String cookiePath = "/";
	
	/**
	 * Inject the cookie path.
	 */
	@Value("#{securityConfig.authenticationPage}")
	protected String authenticationPage = "/authent.jsp";

	// -----------------------------------------------------------------------------------------------------------------
	// Protected methods
	
	/**
	 * Extract scopes from request.
	 * 
	 * @param scopes The String value of scopes.
	 * @return List of scopes.
	 * @throws ProtocolException INVALID_SCOPE: if the scope parameter is not well formated, or if it's not empty.
	 */
	protected List<String> extractScopes(String scopes) {
		List<String> scopesList = new ArrayList<String>();
		// Scopes are optional
		if (scopes != null) {
			// Test scope syntax.
			if(scopes.length() != 0 && !scopes.matches("^(\\w*\\s)*\\w*$")) {
				logger.debug("[obtainAccessTokenBasicCredentials] malformed scope {}",scopes);
				throw new ProtocolException(Error.INVALID_SCOPE, "Scope must be a whitespace delimited string");
			}
			// Split with spaces, and skip whitespaces 
			String[] scopesArray = scopes.split(" ");
			for(String scope : scopesArray) {
				if(scope != null && scope.length() > 0) {
					scopesList.add(scope);
				}
			}
		}
		return scopesList;
	} // extractScopes().
	
	/**
	 * Creates a response that redirect the user with an error.
	 *  
	 * @param redirectUri Redirection URI.
	 * @param state State reuse while redirecting.
	 * @param exc Error case
	 * 
	 * @return The redirected response.
	 */
	protected Response redirectOnError(String redirectUri, String state, ProtocolException exc) {
		ResponseBuilder builder = Response.status(302);
		URI redirection = null;
		try {
			boolean containsQuestionMark = redirectUri.contains("?");
			redirection = new URI(redirectUri+(containsQuestionMark ? "&": "?")+"error="+exc.errorCase.value()+
					"&state="+state);
		} catch (URISyntaxException exc2) {
			logger.debug("[obtainAccessCode] unable to redirect to {}", redirectUri);
			throw new ProtocolException(Error.INVALID_REQUEST, "malformated redirect_uri or state");			
		}
		builder.location(redirection);
		return builder.build();
	} // redirectOnError().
	
	// -----------------------------------------------------------------------------------------------------------------
	// AuthorizationController inherited methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response obtainAccessCode(String responseType, String clientId, String redirectUri, String scopes, 
				String state) {
		logger.trace("[obtainAccessCode] End-user authentication for clientId '{}', response type '{}', redirect URI " +
				"{} and scopes {}", new Object[]{clientId, responseType, redirectUri, scopes});
		// Only redirection uri problems will lead to an exception.
		if(redirectUri == null) {
			logger.debug("[obtainAccessCode] missing mandatory parameters");
			throw new ProtocolException(Error.INVALID_REQUEST, "response_type, client_id and redirect_uri parameters" +
					" are mandatory");
		}
		// Checks URI format.
		URI redirection;
		try {
			redirection = new URI(redirectUri);
		} catch (URISyntaxException exc) {
			logger.debug("[obtainAccessCode] redirection uri misformated {}", redirectUri);
			throw new ProtocolException(Error.INVALID_REQUEST, "malformated redirect_uri");			
		}
		// TODO check redirection registration, or REDIRECT_URI_MISMATCH
		// Other error case will lead to a redirection.
		Response response = null;
		try {
			// Checks mandatory parameters.
			if(responseType == null || clientId == null) {
				logger.debug("[obtainAccessCode] missing mandatory parameters");
				throw new ProtocolException(Error.INVALID_REQUEST, "response_type, client_id and redirect_uri parameters" +
						" are mandatory");
			} 
			// Checks responseType
			if(responseType.compareTo("code") != 0) {	
				logger.debug("[obtainAccessCode] unsupported response_Type {}", responseType);
				throw new ProtocolException(Error.UNSUPPORTED_RESPONSE_TYPE, "Only response_type 'code' is supported");
			}	
			if(clientId.compareTo("") != 0) {	
				logger.debug("[obtainAccessCode] non-empty client id {}", clientId);
				throw new ProtocolException(Error.INVALID_CLIENT, "For now, client id must be empty");
				// TODO check client_id validity (INVALID_CLIENT), and authorization (UNAUTHORIZED_CLIENT)
			}	
			// Checks scope
			extractScopes(scopes);
		} catch (ProtocolException exc) {
			// Redirect user with error.
			response = redirectOnError(redirectUri, state, exc);
		}
		
		if (response == null) {
			logger.debug("[obtainAccessCode] unable to redirect to {}", authenticationPage);
			// No errors, lets send a page to authenticate user.
			ResponseBuilder builder = Response.status(302);
			try {
				boolean containsQuestionMark = authenticationPage.contains("?");
				redirection = new URI(authenticationPage+(containsQuestionMark ? "&": "?")+"redirect_uri="+redirectUri+
						"&state="+state);
			} catch (URISyntaxException exc2) {
				logger.debug("[obtainAccessCode] unable to redirect to {}", redirectUri);
				throw new ProtocolException(Error.INVALID_REQUEST, "malformated redirect_uri or state");			
			}
			builder.location(redirection);
			response = builder.build();
		}
		return response;	
	} // obtainAccessCode().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response obtainAccessTokenBasicCredentials(String clientId,
			String clientSecret, String grant, String scopes, String userName,
			String password) {
		logger.trace("[obtainAccessTokenBasicCredentials] Token generation for clientId '{}', user name '{}', grant " +
				"{} and scopes {}", new Object[]{clientId, userName, grant, scopes});
		// Checks mandatory parameters.
		if(grant == null || userName == null || password == null) {
			logger.debug("[obtainAccessTokenBasicCredentials] missing mandatory parameters");
			throw new ProtocolException(Error.INVALID_REQUEST, "grant_type, username and password parameters are " +
					"mandatory");
		} 
		if (clientId == null || clientSecret == null) {
			logger.debug("[obtainAccessTokenBasicCredentials] missing client credentials");
			throw new ProtocolException(Error.INVALID_CLIENT, "client_id and client_secret parameters are mandatory");

		}
		// Checks grant_type
		if(grant.compareTo("password") != 0) {	
			logger.debug("[obtainAccessTokenBasicCredentials] unsupported grant-type {}", grant);
			throw new ProtocolException(Error.UNSUPPORTED_GRANT_TYPE, "Only grant_type 'password' is supported");
		}		
		// Checks clientId and clientSecret
		if(clientId.compareTo("") != 0 || clientSecret.compareTo("") != 0) {	
			logger.debug("[obtainAccessTokenBasicCredentials] non-empty client credentials {}", clientId);
			throw new ProtocolException(Error.INVALID_CLIENT, "For now, client id and secret must be empty");
		}		
		// Checks scope
		List<String> scopesList = extractScopes(scopes);
		

		// Calls the service layer.
		Token token = null;
		try {
			token = service.generateToken(scopesList, userName, password);
		} catch (IllegalArgumentException exc) {
			logger.debug("[obtainAccessTokenBasicCredentials] invalid parameter: {}", exc.getMessage());
			throw new ProtocolException(Error.INVALID_REQUEST, "grant_type, client_id, client_secret, username and " +
				"password parameters are mandatory");
		}
		logger.trace("[obtainAccessTokenBasicCredentials] Generated token: {}", token);
		// Builds a 200 response.
		ResponseBuilder builder = Response.status(Status.OK);
		// Response body.
		builder.entity(new TokenResponse(token, scopes));
		// Cache control
		CacheControl noCache = new CacheControl();
		noCache.setNoStore(true);
		builder.cacheControl(noCache);
		
		// Sets cookie.
		builder.cookie(new NewCookie("oauth_token", token.accessToken, cookiePath, cookieDomain, "", token.lifeTime, 
				false));
		// Sends response.
		return builder.build(); 
	} // obtainAccessTokenBasicCredentials().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token obtainTokenInformation(String accessToken, String password) {
		// Checks password
		if (password == null || password.compareTo(authorizationPassword) != 0) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		logger.trace("[obtainTokenInformation] Token retrieval for accessToken '{}'", accessToken);
		// Checks mandatory parameters.
		if(accessToken == null) {
			logger.debug("[obtainAccessTokenBasicCredentials] missing mandatory parameters");
			throw new IllegalArgumentException("accessToken parameter is mandatory");
		}
		Token token = service.getTokenInformation(accessToken);
		logger.trace("[obtainTokenInformation] Retrieved token: {}", token);
		return token;
	} // obtainTokenInformation().

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response authenticateEndUser(String username, String password, String redirectUri, String state) {
		logger.trace("[authenticateEndUser] End-user authentication for username '{}'", username);
		// Only redirection uri problems will lead to an exception.
		if(redirectUri == null || username == null || password == null) {
			logger.debug("[authenticateEndUser] missing mandatory parameters");
			throw new ProtocolException(Error.INVALID_REQUEST, "username, password, and redirect_uri parameters" +
					" are mandatory");
		}
		// Checks URI format.
		URI redirection;
		try {
			redirection = new URI(redirectUri);
		} catch (URISyntaxException exc) {
			logger.debug("[authenticateEndUser] redirection uri misformated {}", redirectUri);
			throw new ProtocolException(Error.INVALID_REQUEST, "malformated redirect_uri");			
		}
		Response response = null;
		try {
			// Generate Access code
			String accessCode = service.generateToken(new ArrayList<String>(), username, password).accessCode;	
			// No errors, lets send a page to authenticate user.
			ResponseBuilder builder = Response.status(302);
			try {
				boolean containsQuestionMark = redirectUri.contains("?");
				redirection = new URI(redirectUri+(containsQuestionMark ? "&": "?")+"code="+accessCode+"&state="+state);
			} catch (URISyntaxException exc2) {
				logger.debug("[obtainAccessCode] unable to redirect to {}", redirectUri);
				throw new ProtocolException(Error.INVALID_REQUEST, "malformated redirect_uri or state");			
			}
			builder.location(redirection);
			response = builder.build();
		} catch (ProtocolException exc) {
			if (exc.errorCase == Error.INVALID_CLIENT) {
				// No user fond, access denied.
				exc.errorCase = Error.ACCESS_DENIED;
				response = redirectOnError(redirectUri, state, exc);
			}
		}
		return response;	
	} // authenticateEndUser().
	
} // class AuthorizationControllerImpl