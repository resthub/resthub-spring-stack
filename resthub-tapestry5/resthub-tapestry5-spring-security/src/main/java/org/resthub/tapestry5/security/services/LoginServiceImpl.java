package org.resthub.tapestry5.security.services;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Provide method to dynamically log a user
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public class LoginServiceImpl implements LoginService {

	private AuthenticationManager authenticationManager = null;

	public LoginServiceImpl(final AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * {@inheritDoc}
	 */
	public Boolean executeLogin(String i_username, String i_password,
			List<GrantedAuthority> i_authorities) {
		if (authenticationManager == null) {
			return false;
		}

		try {
			Authentication l_request = new UsernamePasswordAuthenticationToken(
					i_username, i_password, i_authorities);
			Authentication l_result = authenticationManager
					.authenticate(l_request);
			SecurityContextHolder.getContext().setAuthentication(l_result);
		} catch (Exception l_exception) {
			return false;
		}

		return true;
	}

}
