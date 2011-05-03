package org.resthub.identity.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.model.User;
import org.resthub.identity.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Named("identityUserDetailsService")
@Transactional
public class IdentityUserDetailsService implements UserDetailsService {

	@Inject
	@Named("userService")
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		UserDetails userDetails = null;

		try {
			User user = userService.findByLogin(username);

			if (null != user) {
				userDetails = new IdentityUserDetailsAdapter(user);
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}

		if (userDetails == null) {
			throw new UsernameNotFoundException("Returned user is null");
		}
		return userDetails;
	}

}
