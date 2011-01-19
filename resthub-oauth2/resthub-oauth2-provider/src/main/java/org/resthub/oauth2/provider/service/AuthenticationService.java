package org.resthub.oauth2.provider.service;

import java.util.List;

/**
 * This interface is intended to be implemented by a specific business bean.<br/>
 * This interface is used by the AutorizationService to :
 * <ul> 
 * <li>Check the existence of a user account from its username/password.</li>
 * <li>Retrieved a user's permissions</li>
 * </ul>
 */
public interface AuthenticationService  {

	/**
	 * Returns the user's unic identifier from its user name and password.
	 * 
	 * @param userName The searched user name.
	 * @param password The corresponding password.
	 * @return Identifier of the user account. Null if no account found.
	 */
	String getUser(String userName, String password);
	
	/**
	 * Returns the user's permissions.
	 * 
	 * @param userId The user's identifier. Must not be null.
	 * @return The corresponding permissions. May be empty, but not null.
	 */
	List<String> getUserPermissions(String userId);
	
} // interface AuthenticationProvider
