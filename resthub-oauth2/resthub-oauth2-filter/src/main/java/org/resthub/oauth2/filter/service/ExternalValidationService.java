package org.resthub.oauth2.filter.service;

import javax.ws.rs.core.HttpHeaders;

import org.resthub.oauth2.provider.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * Implementation of the token validation service.
 */
public class ExternalValidationService implements ValidationService {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes

	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Url of the central authorization service.
	 */
	protected String tokenInformationEndpoint = "";
	
	/**
	 * Name of the parameter used to pass the access token to the central authorization service.
	 */
	protected String accessTokenParam = "";

	/**
	 * Password used to get token information on the central authorization service.
	 */
	protected String authorizationPassword = "";

	/**
	 * Object used to communicates with the authorization service.
	 */
	protected WebResource authorizationService = null;
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public methods

	/**
	 * Used by Spring to inject the Url of the central authorization service.
	 * 
	 * @param tokenInformationEndpoint The url.
	 */
	public void setTokenInformationEndpoint(String tokenInformationEndpoint) {
		this.tokenInformationEndpoint = tokenInformationEndpoint;
	} // setTokenInformationEndpoint().

	/**
	 * Used by Spring to inject the name of the parameter used to pass the access token to the central authorization 
	 * service.
	 * 
	 * @param accessTokenParam The parameter name.
	 */
	public void setAccessTokenParam(String accessTokenParam) {
		this.accessTokenParam = accessTokenParam;
	} // setAccessTokenParam().

	/**
	 * Used by Spring to inject the password used to get token information on the central authorization service.
	 * 
	 * @param authorizationPassword The central authorization service password
	 */
	public void setAuthorizationPassword(String authorizationPassword) {
		this.authorizationPassword = authorizationPassword;
	} // setAuthorizationPassword().
	
	/**
	 * Post initialization method.
	 * 
	 * Initialize the HTTP client with the central service. 
	 */
	public void postInit() {
		logger.debug("[getByAccessToken] Hit on endpoint '{}'", tokenInformationEndpoint);
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		authorizationService = client.resource(tokenInformationEndpoint);		
	} // postInit().
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public inherited methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token validateToken(String accessToken) {
		logger.trace("[getByAccessToken] Retrieves information about token '{}'", accessToken);
		// Gets the token on the central service.
		// Perform the request.
		Token token = null;
		try {
			token = authorizationService.queryParam(accessTokenParam, accessToken).
				header(HttpHeaders.AUTHORIZATION, authorizationPassword).
				get(Token.class);
		} catch (UniformInterfaceException exc) {
			logger.warn("[getByAccessToken] Cannot retrieved information on token '" + accessToken + "'", exc);
		} catch (Exception exc) {
			logger.warn("[getByAccessToken] Error while retrieving information on token '" + accessToken + "'", exc);
		}
		return token;
	} // validateToken()

} // class ValidationServiceImpl
