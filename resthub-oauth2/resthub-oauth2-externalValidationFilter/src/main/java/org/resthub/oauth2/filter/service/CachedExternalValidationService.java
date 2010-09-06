package org.resthub.oauth2.filter.service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.resthub.oauth2.common.model.Token;

/**
 * Implementation of the token validation service, that caches token during their lifetime to avoid unnecessary calls to
 * the central authorization service.
 */
public class CachedExternalValidationService extends ExternalValidationService {

	// -----------------------------------------------------------------------------------------------------------------
	// Private attributes

	/**
	 * Memory cacghe for tokens.
	 */
	protected ConcurrentMap<String, Token> cache = new ConcurrentHashMap<String, Token>();

	/**
	 * Number of seconds an expired token is kept in the cache.
	 */
	protected Integer discardPeriod = 0;
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public methods

	/**
	 * Used by Spring to inject the cache discard period
	 * 
	 * @param discardPeriod Number of seconds an expired token is kept in the cache.
	 */
	public void setDiscardPeriod(Integer discardPeriod) {
		this.discardPeriod = discardPeriod;
	} // setDiscardPeriod().

	// -----------------------------------------------------------------------------------------------------------------
	// Public inherited methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token validateToken(String accessToken) {
		// Looks for the token into cache.
		Token cachedToken = cache.get(accessToken);
		if(cachedToken == null) {
			// Gets the token from the central Service.
			cachedToken = super.validateToken(accessToken);
			// Stores into cache.
			if(cachedToken != null) {
				logger.trace("[getByAccessToken] Caches value for access token '{}'", accessToken);
				cache.put(accessToken, cachedToken);
			}
		} else {
			logger.trace("[getByAccessToken] Returns cached value for access token '{}'", accessToken);
			// Delete from cache if expired.
			Long expired = (new Date().getTime()  - cachedToken.createdOn.getTime()) / 1000;
			if (expired > (cachedToken.lifeTime + discardPeriod)) {
				cache.remove(accessToken);
				logger.trace("[getByAccessToken] Discards from cache access token '{}'", accessToken);
			}
		}
		return cachedToken;
	} // validateToken().

} // class CachedExternalValidationService
