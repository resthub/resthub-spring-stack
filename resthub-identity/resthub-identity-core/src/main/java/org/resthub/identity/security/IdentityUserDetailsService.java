package org.resthub.identity.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.model.Role;
import org.resthub.identity.model.User;
import org.resthub.identity.service.RoleService.RoleChange;
import org.resthub.identity.service.UserService;
import org.resthub.identity.service.tracability.ServiceListener;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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

    @PostConstruct
    public void init() {
        userService.addListener(this);
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
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        IdentityUserDetailsAdapter userDetails = null;
        
        if (auth != null && (auth.getPrincipal() instanceof IdentityUserDetailsAdapter)) {
            userDetails = (IdentityUserDetailsAdapter) auth.getPrincipal();

            // Update roles for logged users
            if (userDetails != null
                    && (type.equals(RoleChange.ROLE_ADDED_TO_USER.name()) || type
                            .equals(RoleChange.ROLE_REMOVED_FROM_USER.name()))) {
                User user = (User) arguments[1];
                if (userDetails.getUsername().equals(user.getLogin())) {
                    List<Role> roles = new ArrayList<Role>();
                    userService.getRolesFromRootElement(roles, user);
                    userDetails.setRoles(roles);
                }

                // TODO : test if update is necessary
                // TODO : do the same for group or user update (permission)
            }
        }
    }
}
