package org.resthub.web.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.resthub.core.tools.BeanDetail;
import org.resthub.core.tools.ToolingService;

import com.sun.jersey.api.view.ImplicitProduces;

/**
 * BeanDetailsController publishes a REST webservices used to list all Spring beans discovered 
 * by Spring based on the current classpath and application context files.
 * 
 * Remember to disable this one in production !
 */
@Path("/beans")
@Named("beanDetailsController")
@Singleton
@ImplicitProduces("text/html;qs=5")
public class BeanDetailsController {
	
	@Inject
    @Named("toolingService")
	private ToolingService toolingService;

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getResources() {
		List<BeanDetail> beanDetails = this.toolingService.getBeanDetails();
		return Response.ok(beanDetails.toArray(new BeanDetail[beanDetails.size()])).build();
	}
	
}
