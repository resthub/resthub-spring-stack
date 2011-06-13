#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.webapp.t5.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.resthub.tapestry5.jpa.services.ResthubJPAModule;

/**
 * Configuration for integration with tapestry-jpa library in order to map JPA
 * managed entitymanager to tapestry coercions
 */
@SubModule( { ResthubJPAModule.class })
public class AppJPAModule {

	/**
	 * Configure packages where tapestry will find model objects to coerce
	 */
	public static void contributeJPAEntityPackageManager(
			Configuration<String> configuration) {
		configuration.add("${package}.model");
	}

}
