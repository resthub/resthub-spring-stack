package org.resthub.booking.webapp.t5.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.resthub.tapestry5.jpa.services.ResthubJPAModule;

@SubModule( { ResthubJPAModule.class })
public class BookingJPAModule {

	  public static void contributeJPAEntityPackageManager(Configuration<String> configuration)
	  {
	      configuration.add("org.resthub.booking.model");
	  }
	
}
