package org.resthub.booking.webapp.t5.services;

import nu.localhost.tapestry5.springsecurity.services.RequestInvocationDefinition;
import nu.localhost.tapestry5.springsecurity.services.SaltSourceService;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.resthub.booking.service.UserService;
import org.resthub.booking.webapp.t5.services.security.MyUserDetailsService;
import org.resthub.tapestry5.security.services.EmptySaltSourceImpl;
import org.resthub.tapestry5.security.services.ResthubSecurityModule;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Configuration for integration with tapestry-spring-security a library in order
 * to provide complete and integrated login components
 * 
 * @author bmeurant <Baptiste Meurant>
 */
@SubModule( { ResthubSecurityModule.class })
public class BookingSecurityModule {

	private static final String ANONYMOUS_ROLE = "ROLE_ANONYMOUS";
    private static final String AUTHENTICATED_ROLE = "ROLE_AUTH";

    /**
	 * Configure userDetailsService : service used to provide concrete
	 * authentication
	 * 
	 * @param encoder
	 * @param userService
	 * @return built service
	 */
	public static UserDetailsService buildUserDetailsService(
			@Inject PasswordEncoder encoder,
			@InjectService("userService") UserService userService) {

		return new MyUserDetailsService(userService, encoder);
	}

	/**
	 * Configure spring-secuirty mappings
	 * 
	 * @param configuration
	 */
	public static void contributeFilterSecurityInterceptor(
			Configuration<RequestInvocationDefinition> configuration) {

		configuration.add(new RequestInvocationDefinition("/search/**",
				AUTHENTICATED_ROLE));
		configuration.add(new RequestInvocationDefinition("/hotel/**",
				AUTHENTICATED_ROLE));
		configuration.add(new RequestInvocationDefinition("/booking/**",
				AUTHENTICATED_ROLE));
		configuration.add(new RequestInvocationDefinition("/settings",
				AUTHENTICATED_ROLE));
		configuration.add(new RequestInvocationDefinition("/api/**",
				ANONYMOUS_ROLE));
	}

	public static void contributeProviderManager(
			OrderedConfiguration<AuthenticationProvider> configuration,
			@InjectService("DaoAuthenticationProvider") AuthenticationProvider daoAuthenticationProvider) {

		configuration.add("daoAuthenticationProvider",
				daoAuthenticationProvider);
	}

	/**
	 * Override default salt service : no salt required in this sample
	 * application
	 * 
	 * @param configuration
	 */
	public static void contributeServiceOverride(
			MappedConfiguration<Class<?>, Object> configuration) {

		configuration
				.add(PasswordEncoder.class, new PlaintextPasswordEncoder());
		configuration.add(SaltSourceService.class, new EmptySaltSourceImpl());
	}

}
