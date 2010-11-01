package org.resthub.booking.webapp.t5.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.resthub.tapestry5.services.ResthubModule;
import org.tynamo.jpa.JPASymbols;

/**
 * Configuration for application. Include all necessary submodules
 * 
 * @author bmeurant <Baptiste Meurant>
 */
@SubModule( { BookingSecurityModule.class, ResthubModule.class,
		BookingJPAModule.class })
public class BookingModule {

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, String> configuration) {
		// JPA configurations
		configuration.add(JPASymbols.PERSISTENCE_UNIT, "resthub");

		// global configuration
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "fr,en");
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
		configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");

		// spring-security configuration
		configuration.add("tapestry.default-cookie-max-age", "31536000");
		configuration.add("spring-security.failure.url", "/signin/failed");
		configuration.add("spring-security.accessDenied.url", "/forbidden");
		configuration.add("spring-security.check.url",
				"/j_spring_security_check");
		configuration.add("spring-security.target.url", "/search");
		configuration.add("spring-security.afterlogout.url", "/signin");
		configuration.add("spring-security.rememberme.key", "REMEMBERMEKEY");
		configuration.add("spring-security.loginform.url", "/signin");
		configuration.add("spring-security.force.ssl.login", "false");
		configuration.add("spring-security.anonymous.key",
				"spring_security_anonymous");
		configuration.add("spring-security.anonymous.attribute",
				"anonymous,ROLE_ANONYMOUS");
		configuration.add("spring-security.password.salt", "");

	}

	/**
	 * Define web context path that will be ignored by Tapestry
	 * 
	 * @param configuration
	 */
	public static void contributeIgnoredPathsFilter(
			Configuration<String> configuration) {
		configuration.add("/api/.*");
	}

}