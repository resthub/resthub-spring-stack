package org.resthub.tapestry5.security.services;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

/**
 * This interface defines methods allowing to programmatically and
 * dynamically be able to log a user from its login and password
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public interface LoginService {

	/**
	 * Try to log the user from username, password and authorities
	 * 
	 * @param i_username
	 * @param i_password
	 * @param i_authorities
	 * 
	 * @return true if user successfully loggued, false otherwise
	 */
	public abstract Boolean executeLogin(String i_username, String i_password,
			List<GrantedAuthority> i_authorities);

}