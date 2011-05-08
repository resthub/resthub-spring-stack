package org.resthub.oauth2.test.resourceService;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Simple protected resource.
 */
@Path("/resource")
@Named("resourceController")
@Produces(MediaType.APPLICATION_JSON)
public class ResourceController {
		
	@GET
	@Path("/ping")
	public String sayPing() {
		return "Ping";
	}
	
	@GET
	@Path("/hello")
	public String sayHello() {
		return "Hello";
	}
	
	@GET
	@Path("/goodbye")
	public String sayGoodbye() {
		return "Goodbye";
	}
	
}