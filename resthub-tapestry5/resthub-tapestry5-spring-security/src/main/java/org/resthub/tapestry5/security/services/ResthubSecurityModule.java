package org.resthub.tapestry5.security.services;

import nu.localhost.tapestry5.springsecurity.services.SecurityModule;

import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.ServiceId;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.resthub.tapestry5.security.services.Authenticator;
import org.resthub.tapestry5.security.services.LoginService;
import org.resthub.tapestry5.security.services.LoginServiceImpl;
import org.resthub.tapestry5.security.services.SpringSecurityAuthenticator;
import org.springframework.security.authentication.AuthenticationManager;

@SubModule( { SecurityModule.class })
public class ResthubSecurityModule {

	public static void bind(ServiceBinder binder)
    {
        binder.bind(Authenticator.class, SpringSecurityAuthenticator.class);
    }
	
	@ServiceId("loginService")
	public static LoginService buildLoginService(
			@InjectService("providerManager") AuthenticationManager authenticationManager) {
		LoginService loginService = new LoginServiceImpl(authenticationManager);

		return loginService;
	}
	
}
