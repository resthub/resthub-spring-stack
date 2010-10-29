package org.resthub.booking.webapp.t5.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.resthub.tapestry5.jpa.services.ResthubJPAModule;

/**
 * Configuration for integration with tapestry-jpa library in order to map JPA
 * managed entitymanager to tapestry coercions
 * 
 * @author Baptiste Meurant
 */
@SubModule( { ResthubJPAModule.class })
public class BookingJPAModule {

	/**
	 * Configure packages where tapestry will find model objects to coerce
	 * 
	 * @param configuration
	 */
	public static void contributeJPAEntityPackageManager(
			Configuration<String> configuration) {
		configuration.add("org.resthub.booking.model");
	}

}
