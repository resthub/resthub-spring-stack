#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.webapp.t5.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.resthub.tapestry5.services.ResthubModule;
import org.tynamo.jpa.JPASymbols;

/**
 * Configuration for application. Include all necessary submodules
 */
@SubModule( { ResthubModule.class, AppJPAModule.class })
public class AppModule {

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, String> configuration) {
		// JPA configurations
		configuration.add(JPASymbols.PERSISTENCE_UNIT, "resthub");

		// global configuration
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "fr,en");
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
		configuration.add(SymbolConstants.APPLICATION_VERSION, "${version}");
	}

	/**
	 * Define web context path that will be ignored by Tapestry
	 */
	public static void contributeIgnoredPathsFilter(
		Configuration<String> configuration) {
		configuration.add("/api/.*");
	}

}