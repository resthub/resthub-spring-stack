package org.resthub.oauth2.filter.dao;

import org.resthub.oauth2.provider.model.Token;

/**
 * Tokan Data Access Object.
 */
public interface TokenDao {

	/**
	 * Retrieve all token information from an access token.
	 * 
	 * @param accessToken The access token searched.
	 * @return All token information, or null if no token found.
	 */
	Token getByAccessToken(String accessToken);
	
} // interface TokenDao
