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
    @Override
    public Boolean executeLogin(String username, String password,
            List<GrantedAuthority> authorities) {
        if (authenticationManager == null) {
            return false;
        }

        Authentication request = new UsernamePasswordAuthenticationToken(
                username, password, authorities);
        Authentication result = authenticationManager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(result);

        return true;
    }

}
