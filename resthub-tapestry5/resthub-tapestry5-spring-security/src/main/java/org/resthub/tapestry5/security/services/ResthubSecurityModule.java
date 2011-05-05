package org.resthub.tapestry5.security.services;

import nu.localhost.tapestry5.springsecurity.services.SecurityModule;

import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Provide configuration for Tapestry5 resthub spring security module :
 * contributions of services, behaviours, configuration parameters, libraries,
 * etc.
 * 
 * Basically, configure and build login service
 * 
 * @author bmeurant <Baptiste Meurant>
 * 
 */
@SubModule({ SecurityModule.class })
public class ResthubSecurityModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(Authenticator.class, SpringSecurityAuthenticator.class);
    }

    @ServiceId("loginService")
    public static LoginService buildLoginService(
            @InjectService("providerManager") AuthenticationManager authenticationManager) {
        LoginService loginService = new LoginServiceImpl(authenticationManager);

        return loginService;
    }

}
