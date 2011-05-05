package org.resthub.tapestry5.security.services;

import java.util.ArrayList;
import java.util.List;

import nu.localhost.tapestry5.springsecurity.services.LogoutService;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.resthub.tapestry5.security.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Basic Security Realm implementation that allow to offer a real Tapestry5
 * service based on spring security features
 * 
 * @author bmeurant <Baptiste Meurant>
 * @author karesti
 */
public class SpringSecurityAuthenticator implements Authenticator {

    @Inject
    private RequestGlobals requestGlobals;

    @Inject
    private LoginService loginService;

    @Inject
    private LogoutService logoutService;

    /**
     * {@InheritDoc}
     */
    public void login(String username, String password)
            throws AuthenticationException {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new GrantedAuthorityImpl("ROLE_AUTH"));

        Boolean logged = this.loginService.executeLogin(username, password,
                authorities);

        if (!logged) {
            throw new AuthenticationException(
                    "Authentication process has failed");
        }
    }

    /**
     * {@InheritDoc}
     */
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        UserDetails userDetails;
        try {
            userDetails = (UserDetails) authentication.getPrincipal();
        } catch (ClassCastException e) {

            return false;
        }
        return userDetails != null && !userDetails.getUsername().isEmpty();
    }

    /**
     * {@InheritDoc}
     */
    public void logout() {
        requestGlobals.getRequest().getSession(false).invalidate();
        logoutService.logout();
    }

    /**
     * {@InheritDoc}
     */
    public UserDetails getLoggedUser() {
        UserDetails userDetails = null;

        if (isLoggedIn()) {
            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();
            userDetails = (UserDetails) authentication.getPrincipal();
        }

        return userDetails;
    }

}
