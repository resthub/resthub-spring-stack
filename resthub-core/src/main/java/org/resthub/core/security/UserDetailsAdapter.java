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

package org.resthub.core.security;

import java.util.ArrayList;
import java.util.List;

import org.resthub.core.domain.model.identity.User;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;

/**
 * UserDetailsAdapter is used to expose users from Resthub domain model with UserDetails
 * in order to be used with Spring Security
 * 
 * @author Bouiaw
 */
public class UserDetailsAdapter implements UserDetails {

	private static final long serialVersionUID = -757605850806816533L;

	private User user;
	
	public UserDetailsAdapter(User user) {
		this.user = user;
	}
	
	public GrantedAuthority[] getAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		for(String permission : user.getUserAndGroupsPermissions()) {
			grantedAuthorities.add(new GrantedAuthorityImpl(permission));
		}
 
		return grantedAuthorities.toArray(new GrantedAuthority[grantedAuthorities.size()]);
	}

	public String getPassword() {
		return user.getPassword();
	}

	public String getUsername() {
		return user.getName();
	}
	
	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

}
