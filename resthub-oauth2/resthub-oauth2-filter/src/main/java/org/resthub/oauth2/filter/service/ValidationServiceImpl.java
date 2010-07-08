package org.resthub.oauth2.filter.service;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;

import org.resthub.oauth2.filter.dao.TokenDao;
import org.resthub.oauth2.provider.exception.ProtocolException;
import org.resthub.oauth2.provider.exception.ProtocolException.Error;
import org.resthub.oauth2.provider.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the token validation service.
 */
@Named("validationService")
@Singleton
public class ValidationServiceImpl implements ValidationService {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes

	/**
	 * Class logger.
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * DAO to gets token informations.
	 */
	@Inject
	protected TokenDao dao;

	// -----------------------------------------------------------------------------------------------------------------
	// Public inherrited methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token validateToken(String headerValue) {
		Token token = null;
		// Extract Authorization Header Request.
		if (headerValue != null) {
			if (headerValue.matches("^Token token=\".*\"$")) {
				// Extracts the token
				String accessToken = headerValue.replace("Token token=\"", "");
				accessToken = accessToken.substring(0, accessToken.length() - 1);
				logger.trace("[doFilter] Accessing with accessToken '{}'", accessToken);
				// Match the token.
				token = dao.getByAccessToken(accessToken);
				// Token is unknown
				if (token == null) {
					logger.trace("[doFilter] Unknown token '{}'", accessToken);
					throw new ProtocolException(Error.INVALID_TOKEN, "Unvalid token");
				} else {
					// Check token expiration
					Date now = new Date();
					Long expired = (now.getTime() - token.createdOn.getTime()) / 1000;
					if (expired > token.lifeTime) {
						logger.trace("[doFilter] Expired token '{}'", accessToken);
						throw new ProtocolException(Error.EXPIRED_TOKEN, "Token has expired "
								+ (expired - token.lifeTime) + "s ago");
					}
				}
			} else {
				// invalid token
				StringBuilder sb = new StringBuilder("The ").append(HttpHeaders.WWW_AUTHENTICATE).append(
						" header is misformated");
				throw new ProtocolException(Error.INVALID_REQUEST, sb.toString());
			}
		} else {
			// No token.
			throw new ProtocolException(Error.UNAUTHORIZED_REQUEST);
		}
		return token;
	} // validateToken()

} // class ValidationServiceImpl
