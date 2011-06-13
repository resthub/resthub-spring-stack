package org.resthub.tapestry5.security.services;

import nu.localhost.tapestry5.springsecurity.services.SaltSourceService;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Basic implementation of a salt service that provide and empty salt (no salt
 * will be used on password)
 * 
 * @author bmeurant <Baptiste Meurant>
 */
public class EmptySaltSourceImpl implements SaltSourceService {

    public Object getSalt(UserDetails user) {
        return null;
    }

}
