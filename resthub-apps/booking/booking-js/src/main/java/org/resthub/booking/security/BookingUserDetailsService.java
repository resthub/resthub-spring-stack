package org.resthub.booking.security;


import javax.inject.Inject;
import javax.inject.Named;

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
 */
@Named("bookingUserDetailsService")
public class BookingUserDetailsService implements UserDetailsService {

	@Inject
	@Named("userService")
	private UserService userService;

	@SuppressWarnings("unused")
	private PasswordEncoder encoder;
	
	public BookingUserDetailsService() {
		
	}

	public BookingUserDetailsService(UserService userService, PasswordEncoder encoder) {
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

		BookingUserDetails securedUser = new BookingUserDetails(name);
		securedUser.setPassword(user.getPassword());
		securedUser.addAuthority(new GrantedAuthorityImpl("ROLE_AUTH"));

		return securedUser;

	}
}
