package org.resthub.identity.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.model.User;
import org.resthub.identity.service.RoleService;
import org.resthub.identity.service.UserService;
import org.resthub.identity.service.tracability.ServiceListener;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Named("identityUserDetailsService")
@Transactional
public class IdentityUserDetailsService implements UserDetailsService, ServiceListener {

	@Inject
	@Named("userService")
	private UserService userService;
	
	@Inject
	@Named("roleService")
	private RoleService roleService;

	public void setUserService(UserService userService) {
		this.userService = userService;
		roleService.addListener(this);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		IdentityUserDetailsAdapter userDetails = null;

		try {
			User user = userService.findByLogin(username);

			if (null != user) {
				userDetails = new IdentityUserDetailsAdapter(user);
				userDetails.setRoles(userService.getAllUserRoles(user.getLogin()));
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}

		if (userDetails == null) {
			throw new UsernameNotFoundException("Returned user is null");
		}
		return userDetails;
	}
	
	@Override
	public void onChange(String type, Object... arguments) {
		IdentityUserDetailsAdapter userDetails = (IdentityUserDetailsAdapter)SecurityContextHolder.getContext().getAuthentication();
		
		if(userDetails != null) {
			
			// Update roles for logged users
			if(type.startsWith("ROLE_")) {
				userDetails.setRoles(userService.getAllUserRoles(userDetails.getUsername()));
			}
			
			// TODO : test if update is necessary
			// TODO : do the same for group or user update (permission)

		}		
	}

}
