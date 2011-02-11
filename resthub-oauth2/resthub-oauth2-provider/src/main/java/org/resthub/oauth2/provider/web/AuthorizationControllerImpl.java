package org.resthub.oauth2.provider.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
 * 
 * Needs a 
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
	protected String cookieName = null;

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
				logger.debug("[obtainAccessToken] malformed scope {}",scopes);
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
		// somehow if we get here, the previously set token might be invalid
		// unset it to avoid looping between the client and us
		if (cookieName != null) {
			builder.cookie(new NewCookie(cookieName, "", cookiePath, cookieDomain, "", 0, false));
		}
		try {
			boolean containsQuestionMark = redirectUri.contains("?");
			StringBuilder address = new StringBuilder(redirectUri).append(containsQuestionMark ? "&": "?").append("error=").
					append(exc.errorCase.value());
			if (state != null && state != "") {
				address.append("&state=").append(state);
			}
			redirection = new URI(address.toString());
		} catch (URISyntaxException exc2) {
			logger.debug("[obtainAccessCode] unable to redirect to {}", redirectUri);
			throw new ProtocolException(Error.INVALID_REQUEST, "malformated redirect_uri or state");			
		}
		builder.location(redirection);
		return builder.build();
	} // redirectOnError().
	
	/**
	 * Redirect the end-user user agent to the redirectURI, with in addition the accessCode and state.
	 * 
	 * @param redirectUri The redirection URI
	 * @param accessCode access code added to the URI.
	 * @param state state (may be null) added to th URI
	 * @return The redirected response.
	 * 
	 * @throws ProtocolException - INVALID_REQUEST if the redirection URI is misformated. 
	 */
	protected Response redirectToIncomingResource(String redirectUri, String accessCode, String state) {
		// No errors, lets send a page to authenticate user.
		ResponseBuilder builder = Response.status(302);
		try {
			boolean containsQuestionMark = redirectUri.contains("?");
			StringBuilder address = new StringBuilder(redirectUri).append(containsQuestionMark ? "&": "?")
					.append("code=")
					.append(accessCode);
			if (state != null && state != "") {
				address.append("&state=").append(state);
			}
			URI redirection = new URI(address.toString());
			builder.location(redirection);
		} catch (URISyntaxException exc2) {
			logger.debug("[redirectToIncomingResource] unable to redirect to {}", redirectUri);
			throw new ProtocolException(Error.INVALID_REQUEST, "malformated redirect_uri or state");			
		}
		return builder.build();
	} // redirectToIncomingResource().
	
	// -----------------------------------------------------------------------------------------------------------------
	// AuthorizationController inherited methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response obtainAccessCode(String responseType, String clientId, String redirectUri, String scopes, 
				String state, HttpServletRequest request) {
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
			// check the Cookie presence.
			Cookie[] cookies = request.getCookies();
			String accessToken = null;
			if (cookies != null && cookieName != null) {
				// Search for the right cookie.
				for (Cookie cookie : cookies) {
					if (cookieName.equals(cookie.getName())) {
						accessToken = cookie.getValue();
						break;
					}
				}
			}
			try {
				if (accessToken != null) {
					logger.debug("[obtainAccessCode] end-user already known {}", accessToken);
					Token token = service.getTokenInformation(accessToken);
					token = service.generateCode(token, redirectUri);
					response = redirectToIncomingResource(redirectUri, token.code, state);
				}
			} catch (ProtocolException exc) {
				// Redirect user with error.
				response = redirectOnError(redirectUri, state, exc);
			} 
			
			if(accessToken == null) {
				logger.debug("[obtainAccessCode] redirect to {}", authenticationPage);
				// No errors, lets send a page to authenticate user.
				ResponseBuilder builder = Response.status(302);
				try {
					boolean containsQuestionMark = authenticationPage.contains("?");
					StringBuilder address = new StringBuilder(authenticationPage)
							.append(containsQuestionMark ? "&": "?")
							.append("redirect_uri=")
							.append(redirectUri);
					if (state != null && state != "") {
						address.append("&state=").append(state);
					}
					redirection = new URI(address.toString());
				} catch (URISyntaxException exc2) {
					logger.debug("[obtainAccessCode] unable to redirect to {}", redirectUri);
					throw new ProtocolException(Error.INVALID_REQUEST, "malformated redirect_uri or state");			
				}
				builder.location(redirection);
				response = builder.build();
			}
		}
		return response;	
	} // obtainAccessCode().
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Response obtainAccessToken(String clientId,
			String clientSecret, String grant, String scopes, String userName,
			String password, String code, String redirectUri) {
		logger.trace("[obtainAccessToken] Token generation for clientId '{}', user name '{}', grant " +
				"{} and scopes {}", new Object[]{clientId, userName, grant, scopes});
		// Checks mandatory parameters.
		if (clientId == null || clientSecret == null) {
			logger.debug("[obtainAccessToken] missing client credentials");
			throw new ProtocolException(Error.INVALID_CLIENT, "client_id and client_secret parameters are mandatory");

		}
		if(grant == null) {
			logger.debug("[obtainAccessToken] missing mandatory parameters");
			throw new ProtocolException(Error.INVALID_REQUEST, "grant_type, username and password parameters are " +
					"mandatory");
		} 
		// Checks grant_type
		if(grant.compareTo("password") != 0 && grant.compareTo("authorization_code") != 0) {	
			logger.debug("[obtainAccessToken] unsupported grant-type {}", grant);
			throw new ProtocolException(Error.UNSUPPORTED_GRANT_TYPE, "Only grant_type 'password' and " +
					"'authorization_code' are supported");
		}		
		// Checks clientId and clientSecret
		if(clientId.compareTo("") != 0 || clientSecret.compareTo("") != 0) {	
			logger.debug("[obtainAccessToken] non-empty client credentials {}", clientId);
			throw new ProtocolException(Error.INVALID_CLIENT, "For now, client id and secret must be empty");
		}		
		// Checks scope
		List<String> scopesList = extractScopes(scopes);
		
		Token token = null;
		if ("password".equals(grant)) {
			if (userName == null || password == null) {
				logger.debug("[obtainAccessToken] missing mandatory parameters");
				throw new ProtocolException(Error.INVALID_REQUEST, "username and password parameters are mandatory");
			}
			// Calls the service layer.
			token = service.generateToken(scopesList, userName, password, redirectUri);
		} else if ("authorization_code".equals(grant)) {
			if (code == null || redirectUri == null) {
				logger.debug("[obtainAccessToken] missing mandatory parameters");
				throw new ProtocolException(Error.INVALID_REQUEST, "code and redirect_uri parameters are mandatory");
			}
			// Calls the service layer.
			token = service.getTokenFromCode(code, redirectUri);
		}
		logger.trace("[obtainAccessToken] Generated token: {}", token);
		// Builds a 200 response.
		ResponseBuilder builder = Response.status(Status.OK);
		// Response body.
		builder.entity(new TokenResponse(token, scopes));
		// Cache control
		CacheControl noCache = new CacheControl();
		noCache.setNoStore(true);
		builder.cacheControl(noCache);
		
		// Sets cookie.
		if (cookieName != null) {
			builder.cookie(new NewCookie(cookieName, token.accessToken, cookiePath, cookieDomain, "", token.lifeTime, 
					false));
		}
		// Sends response.
		return builder.build(); 
	} // obtainAccessToken().
	
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
			logger.debug("[obtainTokenInformation] missing mandatory parameters");
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
		try {
			URI redirection = new URI(redirectUri);
		} catch (URISyntaxException exc) {
			logger.debug("[authenticateEndUser] redirection uri misformated {}", redirectUri);
			throw new ProtocolException(Error.INVALID_REQUEST, "malformated redirect_uri");			
		}
		Response response = null;
		try {
			// Generate Access code
			String accessCode = service.generateToken(new ArrayList<String>(), username, password, redirectUri).code;
			response = redirectToIncomingResource(redirectUri, accessCode, state);
		} catch (ProtocolException exc) {
			if (exc.errorCase == Error.INVALID_GRANT) {
				// No user fond, access denied.
				exc.errorCase = Error.ACCESS_DENIED;
				response = redirectOnError(redirectUri, state, exc);
			}
		}
		return response;	
	} // authenticateEndUser().
	
} // class AuthorizationControllerImpl