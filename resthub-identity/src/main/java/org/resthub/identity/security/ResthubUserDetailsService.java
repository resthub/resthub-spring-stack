/**
 *  This file is part of Resthub.
 *
 *  Resthub is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *   Resthub is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Resthub.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.resthub.identity.security;

import javax.annotation.Resource;

import org.resthub.core.service.ResourceService;
import org.resthub.identity.domain.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * ResthubUserDetailsService retreive a user from its username in order to allow Spring
 * Security to check authentication rights
 * 
 * @author bouiaw
 */
@Service("resthubUserDetailsService")
public class ResthubUserDetailsService implements UserDetailsService {
	
	private final String ROOT_USERNAME = "admin";
	private final String ROOT_PASSWORD = "admin";

	@Resource
	private ResourceService userService;
	
	public void setUserService(ResourceService userService) {
		this.userService = userService;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		UserDetails loadedUser = null;
		
		try {
			User loadedResthubUser = null;
			
			// For the moment, root user is hard coded in order to have always access to the backoffice
			// even if all users have been removed
			if(username.equals(ROOT_USERNAME)) {
				loadedResthubUser = createRootUser();
			} else {
				loadedResthubUser = (User)this.userService.retreive(username);
			}
					
			if(null != loadedResthubUser) {
				loadedUser = new UserDetailsAdapter(loadedResthubUser);
			}
		} catch(Exception e ) {
 			throw new UsernameNotFoundException(e.getMessage());
		}
		
		if(loadedUser == null) {
			throw new UsernameNotFoundException("Returned user is null");
		}
		
		return loadedUser;
	}
	
	private User createRootUser() {
		User root = new User(ROOT_USERNAME);
		root.setPassword(ROOT_PASSWORD);
		root.setPath("/users/children/admin");
		root.addPermission("ROLE_LOGIN_BACKOFFICE");
			
		return root;
	}

}
