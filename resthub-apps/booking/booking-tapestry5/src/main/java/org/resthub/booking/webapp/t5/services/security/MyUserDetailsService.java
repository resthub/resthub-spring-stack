package org.resthub.booking.webapp.t5.services.security;

import org.resthub.booking.model.User;
import org.resthub.booking.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Service allowing to store, retrive and check credential from application
 * services for a user provided by spring-security. Password check is done by
 * spring.
 * 
 * Initializing authorities
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public class MyUserDetailsService implements UserDetailsService {

	private final UserService userService;
	
	@SuppressWarnings("unused")
	private final PasswordEncoder encoder;

	public MyUserDetailsService(UserService userService, PasswordEncoder encoder) {
		super();
		this.userService = userService;
		this.encoder = encoder;
	}

	/**
	 * {@InheritDoc}
	 */
	public UserDetails loadUserByUsername(String name)
			throws UsernameNotFoundException, DataAccessException {

		User user = userService.findByUsername(name);

		if (null == user) {
			throw new UsernameNotFoundException("user not found in database");
		}

		MyUserDetailsImpl securedUser = new MyUserDetailsImpl(name);
		securedUser.setPassword(user.getPassword());
		securedUser.addAuthority(new GrantedAuthorityImpl("ROLE_AUTH"));

		return securedUser;

	}
}
