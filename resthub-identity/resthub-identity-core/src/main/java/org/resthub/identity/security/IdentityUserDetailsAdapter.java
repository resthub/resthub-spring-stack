package org.resthub.identity.security;

import java.util.ArrayList;
import java.util.List;

import org.resthub.identity.model.User;
import org.resthub.identity.tools.PermissionsOwnerTools;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

public class IdentityUserDetailsAdapter implements UserDetails {

	private static final long serialVersionUID = -624970668241079594L;
	
	private User user;
	
	public User getUser() {
		return user;
	}

	List<GrantedAuthority> grantedAuthorities;
    
    public IdentityUserDetailsAdapter(User user) {
            this.user = user;
            
            // Initialized here in order to avoid a LazyException thrown bythe Group retreival
            grantedAuthorities = new ArrayList<GrantedAuthority>();
            for(String permission : PermissionsOwnerTools.getInheritedPermission(user)) {
                    grantedAuthorities.add(new GrantedAuthorityImpl(permission));
            }            
    }
    
    public List<GrantedAuthority> getAuthorities() {
            return this.grantedAuthorities;
    }

    public String getPassword() {
            return user.getPassword();
    }

    public String getUsername() {
            return user.getLogin();
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
