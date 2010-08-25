package org.resthub.oauth2.test.resourceService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Simple protected resource.
 */
@Path("/")
@Named("resourceController")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class ResourceController {
		
	@GET
	@RolesAllowed("ADMIN")
	public String sayHello() {
		return "Hello World";
	} // sayHello().

} // class ResourceController