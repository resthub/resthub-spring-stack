package org.resthub.oauth2.filter.mock.resource;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

@Path("/")
public class MockController {
	
	/**
	 * Simply returns a string.
	 * @return "Hello world".
	 */
	@GET
	@PermitAll
	public String helloWorld() {
		return "Hello world";
	} // helloWorld()

	/**
	 * Simply returns a string.
	 * @return "Hello world Admin".
	 */
	@GET
	@Path("admin")
	@RolesAllowed({"ADMIN", "USER"})
	public String helloWorldAdmin(@HeaderParam("user_id") String userId, @Context HttpServletRequest request) {
		if (request.getUserPrincipal().getName().equals(userId)) {
			return "Hello world Admin";
		} else {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
	} // helloWorldAdmin()

	/**
	 * Simply returns a string.
	 * @return "Hello world Admin".
	 */
	@POST
	@Path("postadmin")
	@RolesAllowed({"ADMIN", "USER"})
	public String helloWorldPostAdmin(@HeaderParam("user_id") String userId, @Context HttpServletRequest request) {
		if (request.getUserPrincipal().getName().equals(userId)) {
			return "Hello world Admin";
		} else {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
	} // helloWorldPostAdmin()

	/**
	 * Simply returns a string.
	 * @return "Hello world".
	 */
	@POST
	@Path("post")
	@PermitAll
	public String helloWorldPost() {
		return "Hello world";
	} // helloWorldPost()

	/**
	 * Simply returns a string.
	 * @return "Hello world Other".
	 */
	@GET
	@Path("other")
	@RolesAllowed("OTHER")
	public String helloWorldOther() {
		return "Hello world Other";
	} // helloWorldOther()

	/**
	 * Simply returns a string.
	 * @return "Hello world Secured".
	 */
	@GET
	@Path("secured")
	@DenyAll
	public String helloWorldSecured() {
		return "Hello world Secured";
	} // helloWorldSecured()

	
} // Class MockController.