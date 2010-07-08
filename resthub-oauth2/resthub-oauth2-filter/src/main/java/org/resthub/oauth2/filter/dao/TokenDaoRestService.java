package org.resthub.oauth2.filter.dao;

import org.resthub.oauth2.provider.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * Implementation of Token DAO which makes a call to the Authorization service.
 */
public class TokenDaoRestService implements TokenDao {

	// -----------------------------------------------------------------------------------------------------------------
	// Protected attributes

	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Url of the central authorization service.
	 */
	@Value("#{securityConfig.tokenInformationEndpoint}")
	protected String tokenInformationEndpoint = "";
	
	/**
	 * Name of the parameter used to pass the access token to the central authorization service.
	 */
	@Value("#{securityConfig.accessTokenParam}")
	protected String accessTokenParam = "";

	// -----------------------------------------------------------------------------------------------------------------
	// Public TokenDao inherited methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token getByAccessToken(String accessToken) {
		logger.trace("[getByAccessToken] Retrieves information about token '{}'", accessToken);
		// Gets the token on the central service.
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		logger.trace("[getByAccessToken] Hit on endpoint '{}'", tokenInformationEndpoint);
		WebResource resource = client.resource(tokenInformationEndpoint);
		// Perform the request.
		Token token = null;
		try {
			token = resource.queryParam(accessTokenParam, accessToken).get(Token.class);
		} catch (UniformInterfaceException exc) {
			logger.warn("[getByAccessToken] Cannot retrieved information on token '" + accessToken + "'", exc);
		} catch (Exception exc) {
			logger.warn("[getByAccessToken] Error while retrieving information on token '" + accessToken + "'", exc);
		}
		return token;
	} // getByAccessToken().
	
} // class TokenDaoRestService.
